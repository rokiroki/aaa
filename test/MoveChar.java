import java.io.*;

public class MoveChar{
	private File file;
	private char[] map;
	private char[] destination;
	private String line1;
	private String line2;
	private String line3;
	private int now;
	public MoveChar(File f){
		line1 = "";
		line2 = "";
		line3 = "";
		map = new char[12];
		destination = new char[12];
		file = f;
		now = 0;
		readMap();
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
		saveResult();
	}
	public void moveDown(){
		//change with the one i+4;
		char tmp = map[now];
		map[now] = map[now+4];
		now = now + 4;
		map[now] = tmp;
		saveResult();
	}
	public void moveLeft(){
		//change with the one i-1;
		char tmp = map[now];
		map[now] = map[now-1];
		now = now - 1;
		map[now] = tmp;
		saveResult();
	}
	public void moveRight(){
		//change with the one i+1;
		char tmp = map[now];
		map[now] = map[now+1];
		now = now + 1;
		map[now] = tmp;
		saveResult();
	}
	//main function
	public void move(){
		//now<4 can't move up
		//now>7 can't move down
		//now%4=0 can't move left
		//now%4=3 can't move right
		while(!compare()){
			
		}
	}
	//search function
	public void search(int n){
		if(n>0){
			
		}
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
	}
	//compare
	public boolean compare(){
		String maps = new String(map);
		String dess = new String(destination);
		System.out.println(maps+"  "+dess);
		if(maps.equals(dess)){
			return true;
		}
		return false;
	}
	public static void main(String[] args) throws IOException{
		File folder = new File("solution");
		File[] files = folder.listFiles();
		for(int i=0;i<files.length;i++){
			MoveChar mc = new MoveChar(files[i]);
			mc.printResult();
		}
	}
	
}
