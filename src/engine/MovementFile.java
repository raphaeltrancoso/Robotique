package engine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MovementFile {
	private String pathFile;
	
	public MovementFile(String pathFile){
		this.pathFile = pathFile;
	}
	
	public void save(String text, boolean append){
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(pathFile, append));
			writer.write(text);
			writer.newLine();
			writer.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}	
	}
	
	public void load(){
		String line, fields[];
		try {
			BufferedReader reader = new BufferedReader(new FileReader(pathFile));
			while ((line = reader.readLine()) != null) {
				fields = line.split(",");
				System.out.println("X=" + fields[0] + ", Y=" + fields[1]);
			}
			reader.close();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public String getPathFile() {
		return pathFile;
	}
	
	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

}
