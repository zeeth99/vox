package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@SuppressWarnings("serial")
public class WordList extends ArrayList<String> {
	protected String _category;
	protected File _file;
	private ReviewList _reviewList;

	public WordList(File f, String category) {
		_category = category;
		_file = f;
	}

	public void setup() throws FileNotFoundException {
		Scanner sc = new Scanner(_file);
		boolean categoryExists = false;
		while (sc.hasNextLine()) {
			if (sc.nextLine().equals("%" + _category)) {
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

	public List<String> reviewWords(int quizSize) {
		return _reviewList.randomWords(quizSize);
	}

	public String toString() {
		String s = _file+"/"+_category;
		if (_file.toString().startsWith("wordlists/"))
			s = s.substring(10);
		if (_file.toString().startsWith(".history/"))
			s = s.substring(9);
		return s;
	}
	
	public String category() {
		return _category;
	}
	
	public String file() {
		return _file.getName();
	}
}
