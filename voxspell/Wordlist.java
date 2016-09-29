package voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("serial")
public class Wordlist extends ArrayList<String> {
	private String _category;
	
	public Wordlist(File f, String category) throws FileNotFoundException {
		_category = category;
		
		boolean categoryExists = false;
		Scanner sc = new Scanner(f);
		while (sc.hasNextLine()) {
			if (sc.nextLine().equals("%" + category)) {
				categoryExists = true;
				break;
			}
		}
		if (!categoryExists) {
			//TODO: category not found !!
		}
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.charAt(0) == '%')
				break;
			add(line);
		}
		sc.close();
	}

	
	public List<String> randomWords(int quizSize) {
		List<String> tempList = new ArrayList<String>();
		tempList.addAll(this);
		List<String> wordList = new ArrayList<String>();
		Random rnd = new Random();
		for (int i = 0; i < quizSize; i++) {
			if (tempList.isEmpty())
				break;
			String word = tempList.get(rnd.nextInt(tempList.size()));
			tempList.remove(word);
			wordList.add(word);
		}
		return wordList;
	}

	public String toString() {
		return _category; // TODO: might change?
	}
}
