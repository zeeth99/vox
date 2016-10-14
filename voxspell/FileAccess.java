package voxspell;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import voxspell.quiz.Festival;
import voxspell.quiz.WordList;

public class FileAccess {

	final public static File WORDFOLDER = new File("wordlists");
	final public static File STATSFOLDER = new File(".history");
	final public static File FESTIVALFOLDER = new File(".festival");
	
	public static void updateStats(boolean correct, String word, WordList w) {
		// stats files are stored in the following format:
		// {word} {number of times the word was successfully attempted} {number of times the word was attempted}
		// a folder is created for the stats for each wordlist file.
		try {
			boolean wordFound = false;
			File folder = new File(".history/"+w.file());
			if (!folder.isDirectory())
				folder.mkdir();
			File tempFile = new File(".history/.tempFile");
			File inputFile = new File(".history/"+w+".stats");
			inputFile.createNewFile();
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				// Update stats line with current word.
				if (!wordFound && currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					int timesCorrect = Integer.parseInt(brokenLine[1]);
					int timesAttempted = Integer.parseInt(brokenLine[2]) + 1;
					if (correct)
						timesCorrect++;
					writer.write(brokenLine[0]+" "+timesCorrect+" "+timesAttempted+System.getProperty("line.separator"));
					wordFound = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFound)
				if (correct) {
					writer.write(word + " 1 1" + System.getProperty("line.separator"));
				} else {
					writer.write(word + " 0 1" + System.getProperty("line.separator"));
				}
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateRecentStats(correct, word, w);
	}

	public static void updateRecentStats(boolean correct, String word, WordList w) {
		// Recent stats are stored in the following format:
		// {word} {0|1} {0|1} {0|1}
		// 1 represents a successful attempt on the word, 0 represents a failed attempt
		// The rightmost number represents the most recent attempt. 
		try {
			boolean wordFound = false;
			File tempFile = new File(".history/.tempFile");
			File inputFile = new File(".history/"+w+".recent");
			inputFile.createNewFile();
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				// Update recent stats line with the current word.
				if (!wordFound && currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					int i = 0;
					if (correct) 
						i = 1;
					writer.write(brokenLine[0] + " " + brokenLine[2] + " " + brokenLine[3] + " " + i + System.getProperty("line.separator"));
					wordFound = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFound) {
				if (correct) {
					writer.write(word + " 1 1 1" + System.getProperty("line.separator"));
				} else {
					writer.write(word + " 0 0 0" + System.getProperty("line.separator"));
				}
			}
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFiles() {
		if (!WORDFOLDER.exists() || !WORDFOLDER.isDirectory())
			WORDFOLDER.mkdir();
		if (!STATSFOLDER.exists() || !STATSFOLDER.isDirectory())
			STATSFOLDER.mkdir();
		if (!FESTIVALFOLDER.exists() || !FESTIVALFOLDER.isDirectory())
			FESTIVALFOLDER.mkdir();
		try {
			new File(".festival/message.scm").createNewFile();
		} catch (IOException e) {}
		try {   
			List<String> linesToWrite = new ArrayList<>();
			linesToWrite.add(Settings.DEFAULT_VOICE);
			Files.write(Festival.SCHEME_FILE.toPath(), linesToWrite); 
		} catch (Exception e) {} 
	}

	public static void addWordList() {
		// TODO Auto-generated method stub
		
	}


}
