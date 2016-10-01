package voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

@SuppressWarnings("serial")
public class ReviewList extends WordList {

	public ReviewList(String category) {
		super(new File(".history/"+category+".review"), category);
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
