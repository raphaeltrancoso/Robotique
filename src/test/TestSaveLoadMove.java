package test;

import java.io.File;
import java.io.IOException;

import engine.MovementFile;

public class TestSaveLoadMove {
	public static void main(String[] args) throws IOException {
		String path = "movement.txt";
		MovementFile mf = new MovementFile(path);
		System.out.println(System.getProperty("user.dir"));
		mf.save("text", false);
//		mf.save("text2", true);
//		mf.load();
	}
}
