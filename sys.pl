#!/usr/bin/perl
use strict;
use Data::Dumper;
use Carp ();
use Net::Ping;
use Proc::ProcessTable;
use Filesys::DiskFree;
use DBI;
use Net::Address::IP::Local;

my $dbh =
  DBI->connect( "dbi:mysql:dmm:192.168.3.125", "root", "root",
    { mysql_auto_reconnect => 1, } )
  or Carp::croak( "connect db failed " . DBI->errstr );

run() unless caller;

sub get_disk_info {
    my ( $path, $disk_total, $disk_free, $quota, $status, $updated );
    my $handle = new Filesys::DiskFree;
    $handle->df();
    my $disk_info = {};

    for my $path ( $handle->disks() ) {

        # turn into how mangy GB
        $disk_info->{$path}{disk_total} =
          $handle->total($path) / ( 1024 * 1024 * 1024 );
        $disk_info->{$path}{disk_free} =
          $handle->avail($path) / ( 1024 * 1024 * 1024 );
        $disk_info->{$path}{quota} = $disk_info->{$path}{disk_total} * 0.1;

        if ( $disk_info->{$path}{disk_free} <= $disk_info->{$path}{quota} ) {
            $disk_info->{$path}{status} = "abnormal";
        }
        else {
            $disk_info->{$path}{status} = "normal";
        }
    }

    return $disk_info;
}

=pod
use Sys::Hostname qw(hostname); # not strictly necessary; exports it b
+y default
use Socket;
my($addr) = inet_ntoa( (gethostbyname(hostname()))[4] );
print "$addr\n";
=cut

sub get_machine_info {
    my ( $network, $cpu, $load_average, $total_process, $perl_process );

    # get local host
    my $local_hostname = Net::Address::IP::Local->public;
    $network = pingecho($local_hostname) ? 'active' : 'inactive';

    # pt process table
    # acount perl process num and total_process
    my $pt = new Proc::ProcessTable( 'cache_ttys' => 1 );
    for my $table ( @{ $pt->table } ) {
        $total_process++;

        if ( $table->{exec} eq '/usr/bin/perl' ) {
            $perl_process++;
        }
    }

# fetch cpu load  and average
# top - 15:39:15 up  1:38,  3 users,  load average: 0.07, 0.21, 0.22
# Tasks: 151 total,   1 running, 148 sleeping,   2 stopped,   0 zombie
# Cpu(s):  1.6%us,  1.9%sy,  0.3%ni, 92.6%id,  3.6%wa,  0.0%hi,  0.1%si,  0.0%st
# Mem:   1024784k total,   950928k used,    73856k free,   260596k buffers
# Swap:  1046524k total,     1476k used,  1045048k free,   316160k cached
    my $top_cmd = "top -b -n 1";
    my $top_ret = qx{$top_cmd};
    if (
        $top_ret =~ m{
            load\s+average:\s*([\d\.]+),\s*([\d\.]+),\s*([\d\.]+) # match cpu load av
            .*? # other string
            Cpu.+?([\d\.]+)%id, # match cpu idle
        }six
      )
    {
        $load_average = ( $1 + $2 + $3 ) / 3;
        $cpu          = 1 - $4 / 100;
    }

    return {
        cpu           => $cpu,
        network       => $network,
        load_average  => $load_average,
        total_process => $total_process,
        perl_process  => $perl_process,
        ip            => $local_hostname
    };
}

sub get_process_info {
    my $ip           = shift;
    my $filter       = {};
    my $process_info = {};
    my $sql =
      "SELECT process_name,ip,parameter,status FROM process where ip = ?";
    my $sth = $dbh->prepare($sql);
    $sth->execute($ip) or Carp::croak DBI->errstr;

    while ( my $row = $sth->fetchrow_arrayref() ) {
        my $process_name = $row->[0];
        my $parameter    = $row->[2];

        if ( $parameter =~ m{mysql.+?:(\d+)'} ) {
            my $port = $1;
            $filter->{ $process_name . ":" . $port }->{regex} =
              qr/mysql.*?$port/;
        }
        else {
            $filter->{$process_name}->{regex} = qr/$process_name/;
        }
    }

    # fetch process status
    my $pt = new Proc::ProcessTable( 'cache_ttys' => 1 );
    for my $p ( @{ $pt->table } ) {
        my $cmd = $p->cmndline;
        next
          unless grep { my ($name) = split( ":", $_ ); $cmd =~ m/$name/ }
              keys %$filter;
        map {
            my ( $process_name, $c ) = split( ":", $_ );
            my $status = $p->kill(0) ? 'active' : 'inactive';

            # special process for diff mysql port
            # filter process mysql with mysql start port number
            if ( $process_name eq 'mysql' ) {
               if ( index( $cmd, '33' ) == -1
                    and $filter->{ $process_name . ":3306" } )
                {
                    $process_info->{ $process_name . ":3306" }{status} =
                      $status;
                }
                elsif ( $cmd =~ m/(33\d{2})/ ) {
                    my $port = $1;
                    my $regex =
                      $filter->{ $process_name . ":" . $port }->{regex};
                    if ( $cmd =~ m/$regex/ ) {
                        $process_info->{ $process_name . ":" . $port }{status} =
                          $status;
                    }
                }
            }
            else {
                $process_info->{$process_name}{status} = $status;
            }
          }
          keys %{$filter};
    }

    # check process is exists in process table
    # if not ,assume it is inactive,because the process is not start
    for my $process ( keys %{$filter} ) {
        if ( not exists $process_info->{$process} ) {
            $process_info->{$process}{status} = 'inactive';
        }
    }
    return $process_info;
}
