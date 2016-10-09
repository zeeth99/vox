package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SuppressWarnings("serial")
public class ReviewList extends WordList {

	public ReviewList(File f, String category) {
		super(f, category);
	}
	
	public ReviewList(File f) {
		super(f, f.getName().substring(0, f.getName().length()-".recent".length()));
	}

	public void setup() throws FileNotFoundException {
		Scanner sc = new Scanner(_file);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.charAt(0) == '%')
				break;
			add(line);
		}
		sc.close();
	}
}
