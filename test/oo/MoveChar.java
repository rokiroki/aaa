import java.io.*;
import java.util.*;

public class MoveChar{
	private File file;
	private char[] map;
	private char[] destination;
	private String line1;
	private String line2;
	private String line3;
	private char[] mapbak;
	private int now;
	private int nowbak;
	private ArrayList trace;
	private int count;
	public MoveChar(File f){
		line1 = "";
		line2 = "";
		line3 = "";
		map = new char[12];
		destination = new char[12];
		file = f;
		now = 0;
		count = 1;
		readMap();
		trace = new ArrayList();
		System.out.println("aabbbaaaaa"+new String(mapbak));
	}
	//save result to txt
	public void printResult() throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(line1+"\r\n"+line2+"\r\n"+line3);
		output.close();
		
	}
	//save result
	public void saveResult(){
		line1 += map[0]+""+map[1]+map[2]+map[3]+" ";
		line2 += map[4]+""+map[5]+map[6]+map[7]+" ";
		line3 += map[8]+""+map[9]+map[10]+map[11]+" ";
	}
	public void moveUp(){
		//change with the one i-4;
		char tmp = map[now];
		map[now] = map[now-4];
		now = now - 4;
		map[now] = tmp;
		//saveResult();
	}
	public void moveDown(){
		//change with the one i+4;
		char tmp = map[now];
		map[now] = map[now+4];
		now = now + 4;
		map[now] = tmp;
		//saveResult();
	}
	public void moveLeft(){
		//change with the one i-1;
		char tmp = map[now];
		map[now] = map[now-1];
		now = now - 1;
		map[now] = tmp;
		//saveResult();
	}
	public void moveRight(){
		//change with the one i+1;
		char tmp = map[now];
		map[now] = map[now+1];
		now = now + 1;
		map[now] = tmp;
		//saveResult();
	}
	//main function
	public void move() throws IOException{
		int n=1;
		boolean result = false;

		while(!result){
			count = n;
			result = search(n);
			n++;
			trace.removeAll(trace);
			map = null;
			map = mapbak.clone();
			now = nowbak;
		}
	}
	//search function
	public boolean search(int n) throws IOException{
		while((trace.size()>count-n)&&(trace.size()>0)){
			trace.remove(trace.size()-1);
		}
		if(count == n){
			trace.retainAll(trace);
		}

		if(n>0){
			HashSet al = new HashSet();
			//up---1
			//down--2
			//left--3
			//right--4
			//now<4 can't move up
			//now>7 can't move down
			//now%4=0 can't move left
			//now%4=3 can't move right
			al.add(1);
			al.add(2);
			al.add(3);
			al.add(4);
			if(now<4){
				al.remove(1);
			}
			if(now>7){
				al.remove(2);

			}
			if(now%4==0){
				al.remove(3);

			}
			if(now%4==3){
				al.remove(4);

			}
			Iterator it = al.iterator();
			while(it.hasNext()){
				int nn = n;
				System.out.println("n="+n+" count="+count+" size ="+trace.size());
				char[] mapptmp = map.clone();
				int nowwtmp = now;
				int action = (Integer)it.next();
				switch(action){
				case 1:
					moveUp();
					break;
				case 2:
					moveDown();
					break;
				case 3:
					moveLeft();
					break;
				case 4:
					moveRight();;
				}
				trace.add(action);
				nn--;
				if(n>=0&&!search(nn)){
					map = mapptmp.clone();
					now = nowwtmp;
					while((trace.size()>count-n)&&(trace.size()>0)){
						trace.remove(trace.size()-1);
					}
				}
				
			}
		}else if(n==0){
			System.out.println("aaaaaaan="+n+" count="+count+" size ="+trace.size());
			if(compare()){
				System.out.println("done");
				map = mapbak.clone();
				now = nowbak;
				Iterator ii = trace.iterator();
				while(ii.hasNext()){
					int ttrace = (Integer)ii.next();
					System.out.println(ttrace);
					switch(ttrace){
					case 1:
						moveUp();
						break;
					case 2:
						moveDown();
						break;
					case 3:
						moveLeft();
						break;					
					case 4:
						moveRight();
					}
					System.out.println("aaaaold"+new String(map)+now);
					//saveResult();
					line1 += map[0]+""+map[1]+map[2]+map[3]+" ";
					line2 += map[4]+""+map[5]+map[6]+map[7]+" ";
					line3 += map[8]+""+map[9]+map[10]+map[11]+" ";
					System.out.println(line1);
					System.out.println(line2);
					System.out.println(line3);
					}
				printResult();
				//System.out.println();
				//System.out.println(count);
				System.exit(1);
				return true;
			}else{
				if(trace.size()>0){
					trace.remove(trace.size()-1);
				}
				return false;
			}
		}

		return false;
		
	}
	//read configuration from txt name
	public void readMap(){
		String nameStr = file.getName();
		String[] tmps = nameStr.split("2");
		map = tmps[0].toCharArray();
		destination = tmps[1].split("\\.")[0].toCharArray();
		saveResult();
		for(int i=0;i<11;i++){
			if(map[i] == '_'){
				now = i;
			}
		}
		mapbak = map.clone();
		nowbak = now;
	}
	//compare
	public boolean compare(){
		String maps = new String(map);
		String dess = new String(destination);
		//System.out.println(maps+"  "+dess);
		if(maps.equals(dess)){
			return true;
		}
		return false;
	}
	public static void main(String[] args) throws IOException{
		//File folder = new File("solution");
		//File[] files = folder.listFiles();
		//for(int i=0;i<files.length;i++){
			//MoveChar mc = new MoveChar(files[i]);
			//mc.move();
		//}
		//File f = new File("solution/abbcd_dadddb2abbcd_bdddda.txt");
		//File f = new File("solution/addbd_dbdbca2_ddbadbaddbc.txt");
		File f = new File("solution/baadd_bddcbd2da_adbddcbdb.txt");
		//File f = new File("solution/badddbadcd_b2ba_dcbddddab.txt");
		////File f = new File("solution/bbddc_dbadad2badbd_ddcadb.txt");
		////File f = new File("solution/bdcadddd_abb2_adcbdbadbdd.txt");
		////File f = new File("solution/cdbbddaa_dbd2ddbdcab_ddba.txt");
		////File f = new File("solution/cddbabaddd_b2dbadcbdbad_d.txt");
		///File f = new File("solution/dadbbda_bddc2bddad_abdbdc.txt");
		///File f = new File("solution/dcdda_abbddb2cdddbabb_dad.txt");
		////File f = new File("solution/ddadb_acdbbd2ddaab_dddcbb.txt");
		//File f = new File("solution/dd_adcddabbb2ddadadbb_dcb.txt");
		//File f = new File("solution/ddadd_adbbbc2ad_addddbbbc.txt");
		////File f = new File("solution/ddbdbbadac_d2bbaddddd_bac.txt");
		////File f = new File("solution/dddabbd_abdc2ddcdbbd_adba.txt");
		//File f = new File("solution/_dddadbdbcab2abddcbd_dabd.txt");
		MoveChar m = new MoveChar(f);
		m.move();
	}
	
}
