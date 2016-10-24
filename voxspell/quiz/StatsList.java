package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class StatsList extends ArrayList<WordStats> implements Comparable<StatsList> {
	private File _stats;
	private File _recent;
	private String _name;

	public StatsList(File directory, String name) {
		_name = name;
		_stats = new File(directory+"/"+name+".stats");
		_recent = new File(directory+"/"+name+".recent");
	}

	public void setup() throws FileNotFoundException {
		if (size() > 0) return;

		Scanner sc = new Scanner(_stats);
		while (sc.hasNextLine()) {
			WordStats w = new WordStats(sc.next());
			w._successes = sc.nextInt();
			w._attempts = sc.nextInt();
			add(w);
			sc.nextLine();
		}
		sc.close();
		sc = new Scanner(_recent);
		while (sc.hasNextLine()) {
			WordStats w = new WordStats(sc.next());
			for (WordStats ws : this)
				if (w.equals(ws))
					w = ws;
			int[] i = {sc.nextInt(), sc.nextInt(), sc.nextInt()};
			w._recent = i;
			sc.nextLine();
		}
		sc.close();
	}

	public String toString() {
		return _name;
	}

	public ArrayList<String> wordList() {
		ArrayList<String> list = new ArrayList<String>();
		for (WordStats ws : this)
			list.add(ws._word);
		return list;
	}

	public ArrayList<String[]> statsInfo() {
		ArrayList<String[]> list = new ArrayList<String[]>();

		for (WordStats ws : this) {
			String score = "";
			for (int i : ws._recent)
				if (i == 1)
					score += "\u2605 ";
			String[] s = {ws._word, score, String.valueOf(ws._successes), String.valueOf(ws._attempts)};
			list.add(s);
		}
		return list;
	}

	@Override
	public int compareTo(StatsList arg0) {
		return _name.compareTo(arg0._name);
	}

}

class WordStats {
	String _word;
	int _attempts;
	int _successes;
	int[] _recent;

	public WordStats(String w) {
		_word = w;
	}

	public boolean equals(WordStats w) {
		return _word.equals(w._word);
	}
}
