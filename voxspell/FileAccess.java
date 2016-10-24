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

import javax.swing.JFileChooser;

import voxspell.quiz.Festival;
import voxspell.quiz.WordList;

/**
 * Class which handles writing to files.
 * @author Max McLaren
 */
public class FileAccess {

	final public static File WORDFOLDER = new File("wordlists");
	final public static File STATSFOLDER = new File(".history");
	final public static File FESTIVALFOLDER = new File(".festival");
	final public static File MEDIAFOLDER = new File(".media");
	final private static String NL = System.getProperty("line.separator");

	/**
	 * Updates the statistics files for a given word
	 * @param correct - true if word was answered correctly
	 * @param word - word to have its stats updated
	 * @param w - WordList that the word is in
	 */
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
				if (!wordFound && currentLine.contains(word+" ")) {
					String[] brokenLine = currentLine.split(" ");
					int timesCorrect = Integer.parseInt(brokenLine[1]);
					int timesAttempted = Integer.parseInt(brokenLine[2]) + 1;
					if (correct)
						timesCorrect++;
					writer.write(brokenLine[0]+" "+timesCorrect+" "+timesAttempted+NL);
					wordFound = true;
				} else {
					writer.write(currentLine + NL);
				}
			}
			if (!wordFound)
				if (correct) {
					writer.write(word + " 1 1" + NL);
				} else {
					writer.write(word + " 0 1" + NL);
				}
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			new ErrorMessage(e);
		} catch (IOException e) {
			new ErrorMessage(e);
		}
		updateRecentStats(correct, word, w);
	}

	/**
	 * Updates the statistics .recent files for a given word
	 * @param correct - true if word was answered correctly
	 * @param word - word to have its stats updated
	 * @param w - WordList that the word is in
	 */
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
				if (!wordFound && currentLine.contains(word+" ")) {
					String[] brokenLine = currentLine.split(" ");
					int i = 0;
					if (correct) 
						i = 1;
					writer.write(brokenLine[0] + " " + brokenLine[2] + " " + brokenLine[3] + " " + i + NL);
					wordFound = true;
				} else {
					writer.write(currentLine + NL);
				}
			}
			if (!wordFound && correct)
				writer.write(word + " 0 1 1" + NL);
			if (!wordFound && !correct) 
				writer.write(word + " 0 0 0" + NL);
			
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			new ErrorMessage(e);
		} catch (IOException e) {
			new ErrorMessage(e);
		}
	}

	/**
	 * Create all necessary folders and 
	 */
	public static void createFiles() {
		File[] folders = {WORDFOLDER, STATSFOLDER, FESTIVALFOLDER, MEDIAFOLDER};
		for (File f : folders)
			if (!f.isDirectory())
				f.mkdir();
		try {
			if (Festival.SCHEME_FILE.createNewFile()) {
				List<String> linesToWrite = new ArrayList<>();
				linesToWrite.add(Settings.DEFAULT_VOICE);
				Files.write(Festival.SCHEME_FILE.toPath(), linesToWrite); 				
			}
		} catch (IOException e) {
			new ErrorMessage(e);
		} 
	}

	/**
	 * Adds a file to the folder of word lists.
	 */
	public static void addWordList() {
		JFileChooser chooser = new JFileChooser();
		int returnValue = chooser.showOpenDialog(chooser);
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File fromFile = chooser.getSelectedFile();
			File toFile = new File(WORDFOLDER+"/"+fromFile.getName());
			copyFile(fromFile, toFile);
		}
	}

	/**
	 * Copies contents of a file to another file.
	 * If the to file doesn't exist it is created
	 * @param from - File to copy
	 * @param to - File to be created as a copy of from
	 * @return true if the file was successfully copied, false otherwise
	 */
	private static boolean copyFile(File from, File to) {
		if (!from.isFile()) 
			return false;

		try {
			if (!to.createNewFile())
				return false;

			BufferedReader reader = new BufferedReader(new FileReader(from));
			BufferedWriter writer = new BufferedWriter(new FileWriter(to));

			String currentLine;
			while ((currentLine = reader.readLine()) != null)
				writer.write(currentLine + NL);

			reader.close();
			writer.close();
		} catch (IOException e) {
			new ErrorMessage(e);
		}
		return true;
	}

	public static void clearStats() {
		for (File f : STATSFOLDER.listFiles())
			for (File f1 : f.listFiles())
				f1.delete();
	}
}
