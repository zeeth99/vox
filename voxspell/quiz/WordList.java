package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import voxspell.ErrorMessage;

@SuppressWarnings("serial")
public class WordList extends ArrayList<String> implements Comparable<WordList>{
	protected String _category;
	protected File _file;

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
		if (!categoryExists)
			new ErrorMessage("One or more word list files have been altered and a category has gone missing.\n"
					+"The "+ _category + "category cannot be quizzed.", "Category Missing");
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
		String s = _file+"/"+_category;
		if (_file.toString().startsWith("wordlists"))
			s = s.substring(10);
		return s;
	}

	public String category() {
		return _category;
	}

	public String file() {
		return _file.getName();
	}

	@Override
	public int compareTo(WordList arg0) {
		return toString().compareToIgnoreCase(arg0.toString());
	}
}
