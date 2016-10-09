package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StatsList extends ArrayList<String> {
	private File _file;
	
	public StatsList(File f) {
		_file = f;
	}
	
	public void setup() throws FileNotFoundException {
		Scanner sc = new Scanner(_file);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			add(line);
		}
		sc.close();
	}
}
