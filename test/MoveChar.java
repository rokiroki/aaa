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
		System.out.println(line1);
		System.out.println(line2);
		System.out.println(line3);
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
			result = search(n);
			n++;
			count = n;
			if(result){
				Iterator ii = trace.iterator();
				while(ii.hasNext()){
					System.out.print(ii.next());
				}
				break;
			}else{
				trace.removeAll(trace);
			}
			System.out.println(n+"-------------------------------");
		}
	}
	//search function
	public boolean search(int n) throws IOException{
		while((trace.size()>count-n-1)&&(trace.size()>0)){
			trace.remove(trace.size()-1);
		}
		System.out.println("n="+n+" count="+count+" size ="+trace.size());
		Iterator itt = trace.iterator();
		while(itt.hasNext()){
			System.out.println(itt.next());
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
				char[] mapptmp = map.clone();
				int nowwtmp = now;
				int action = (Integer)it.next();
				switch(action){
				case 1:
					moveUp();
					System.out.println("up"+new String(map)+" "+now);
					break;
				case 2:
					moveDown();
					System.out.println("down"+new String(map)+" "+now);
					break;
				case 3:
					moveLeft();
					System.out.println("left"+new String(map)+" "+now);
					break;
				case 4:
					moveRight();
					System.out.println("right"+new String(map)+" "+now);
				}
				trace.add(action);
				nn = nn-1;
				if(compare()){
					System.out.println("done");
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
						saveResult();
						}
					printResult();
					System.out.println();
					System.out.println(count);
					System.exit(1);
					return true;
				}else{
					
				}
				search(nn);
				map = mapptmp.clone();
				now = nowwtmp;
			}
		}else{
		//recovery if failed
			//trace.remove(trace.size()-1);
		}
		map = null;
		map = mapbak.clone();
		now = nowbak;
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
		System.out.println(now);
		mapbak = map.clone();
		System.out.println("aaaaaaa"+new String(mapbak));
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
		File f = new File("abbcd_dadddb2abbc_ddadddb.txt");
		MoveChar m = new MoveChar(f);
		m.move();
	}
	
}

