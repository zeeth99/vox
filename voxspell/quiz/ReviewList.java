package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import voxspell.ErrorMessage;

/**
 * 
 * @author Max McLaren
 *
 */
@SuppressWarnings("serial")
public class ReviewList extends WordList {
	
	int _maximumScore;

	public ReviewList(File f, String category) {
		super(f, category);
		_maximumScore = 2;
	}

	public ReviewList(File f) {
		super(f, f.getName().substring(0, f.getName().length()-".recent".length()));
	}

	public void setup() {
		this.clear();
		Scanner sc;
		try {
			sc = new Scanner(_file);
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				String[] brokenLine = line.split(" ");
				if (Integer.parseInt(brokenLine[1]) + Integer.parseInt(brokenLine[2]) + Integer.parseInt(brokenLine[3]) < _maximumScore)
					add(brokenLine[0]);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			new ErrorMessage(e);
		}
	}
	
	public void setup(int i) {
		_maximumScore = i;
		setup();
	}

	public String toString() {
		return _file.toString().substring(9, _file.toString().length()-7);
	}
}
