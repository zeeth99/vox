package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

@SuppressWarnings("serial")
public class StatsList extends ArrayList<WordStats> {
	private File _stats;
	private File _recent;
	private String _name;

	public StatsList(File directory, String name) {
		_name = name;
		_stats = new File(directory+"/"+name+".stats");
		_recent = new File(directory+"/"+name+".recent");
	}

	public void setup() throws FileNotFoundException {
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
			WordStats w = this.get(this.indexOf(new WordStats(sc.next())));
			int[] i = {sc.nextInt(), sc.nextInt(), sc.nextInt()};
			w._recent = i;
			sc.nextLine();
		}
		sc.close();
	}

	public String toString() {
		return _name;
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
