package test;

import engine.MovementFile;
import lejos.hardware.Sound;
import lejos.hardware.lcd.LCD;

public class testWriteFile {
	public static void main(String[] arg){
//		while(true){
		boolean first = false;
		for (int i = 0; i < 10; i++) {
			MovementFile mf = new MovementFile("moveTest.txt");
			mf.save("ligne" + i, first);
			first = true;
			System.out.println(System.getProperty("user.dir"));
			LCD.drawString("user.dir", 0, 3);
		}
		
//		}
	}
}
