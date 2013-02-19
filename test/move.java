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
	public void printResult() throws IOException{
		BufferedWriter output = new BufferedWriter(new FileWriter(file));
		output.write(line1+"\n"+line2+"\n"+line3);
		output.close();
		
	}
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
	public void move(){
		//now<4 can't move up
		//now>7 can't move down
		//now%4=0 can't move left
		//now%4=3 can't move right
	}
	public void readMap(){
		String nameStr = file.getName();
		String[] tmps = nameStr.split("2");
		map = tmps[0].toCharArray();
		destination = tmps[1].toCharArray();
		saveResult();
		for(int i=0;i<11;i++){
			if(map[i] == '_'){
				now = i;
			}
		}
	}
	public static void main(String[] args) throws IOException{
		File f = new File("abcd_dcbabcd2dcbaabcd_abc.txt");
		MoveChar mc = new MoveChar(f);
		mc.moveDown();
		mc.moveRight();
		mc.moveUp();
		mc.moveLeft();
		mc.printResult();
	}
	
}
