package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StatsList extends ArrayList<WordStats> {
	private File _directory;
	private File _stats;
	private File _recent;
	private String _name;
	
	public StatsList(File d, String name) {
		_directory = d;
		_name = name;
		String dString = d.getPath();
		_stats = new File(dString+"/"+name+".stats");
		_recent = new File(dString+"/"+name+".recent");
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
