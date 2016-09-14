package quiz;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FileOperation {
	
	private JFrame frame;
	
	/* List of constants describing files which are stored in the same directory as the jar file */
	private static String WORDLIST = ClassLoader.getSystemClassLoader().getResource(".").getPath()+"wordlist.txt";
	private static String REVIEW = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".review.txt";
	private static String MASTERED = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".mastered.txt";
	private static String FAULTED = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".faulted.txt";
	private static String FAILED = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".failed.txt";
	private static String QUIZD = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".quizd.txt";
	private static String TEMP = ClassLoader.getSystemClassLoader().getResource(".").getPath()+".temp.txt";
	
	private List<String> listOfWords = new ArrayList<String>();
	private List<String> reviewList = new ArrayList<>();
	
	public FileOperation(JFrame frame) {
		this.frame = frame;
		createWordList(WORDLIST);
		checkFiles();
		readInReviewWords();
	}
	
	// Checks if files needed to store statistics already exist, if not then
	// ones are created (for the first time this program is run)
	protected void checkFiles() {
		File mastered = new File(MASTERED);
		File review = new File(REVIEW);
		File faulted = new File(FAULTED);
		File failed = new File(FAILED);
		File quizd = new File(QUIZD);
		try{
			mastered.createNewFile();
			review.createNewFile();
			faulted.createNewFile();
			failed.createNewFile();
			quizd.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
	}
	
	// Returns a random word from the word list to be quizzed
	protected String getRandomWordFromWordList() {	
		Random r = new Random();
		return listOfWords.get(r.nextInt(listOfWords.size()));	
	}
	
	// Returns a random word from the list of mistakes to be quizzed in review menu
	protected String getRandomWordFromReview() {
		Random r = new Random();
		if (reviewList.size() == 0) {
			return null;
		} else {
			return reviewList.get(r.nextInt(reviewList.size()));
		}
	}
	
	// Reads a text file and produces a list of words from that file
	protected void createWordList(String fileName) {
		try {
			Scanner sc = new Scanner(new FileReader(fileName));
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (isAWord(word)) {
					listOfWords.add(word);
				} else {
					JOptionPane.showMessageDialog(frame, "wordlist.txt contains an invalid word\nPlease"
							+ " make sure the words contain no leading or trailing spaces or any spaces inbetween"
							+ " a word. Numbers and special characters are also not allowed\nThere should also be no empty lines after the last word in the file, "
							+ " inbetween words, or at the beginning of the file");
					System.exit(1);
				}
			}
			
			if (listOfWords.isEmpty()) {
				JOptionPane.showMessageDialog(frame, "wordlist.txt contains no words. Please populate the list before"
						+ "\nrunning the program");
				System.exit(1);
			}
 			sc.close();
		} catch (FileNotFoundException e) {
			// wordlist.txt not found so system exits with exit code 1
			JOptionPane.showMessageDialog(frame, fileName+" could not be found\nPlease make sure a file named 'wordlist.txt' "
					+ "exists in the current directory: "+ClassLoader.getSystemClassLoader().getResource(".").getPath());
			System.exit(2);
		}
	}
	
	protected List<String> getQuizdWords() {
		List<String> list = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(new FileReader(QUIZD));
			while (sc.hasNextLine()) {
				list.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		return list;
	}
	
	// Reads in words from text file that still need to be reviewed for when
	// the user has closed the program
	protected void readInReviewWords() {
		try {
			Scanner sc = new Scanner(new FileReader(REVIEW));
			while (sc.hasNextLine()) {
				reviewList.add(sc.nextLine());
			}
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		
	}
	
	protected void addWordToMastered(String wordBeingQuizd) {
		try {
			Writer output = new BufferedWriter(new FileWriter(MASTERED, true));
			output.write(wordBeingQuizd+"\n");
			output.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	protected void addWordToFaulted(String wordBeingQuizd) {
		try {
			Writer output = new BufferedWriter(new FileWriter(FAULTED, true));
			output.write(wordBeingQuizd+"\n");
			output.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	protected void addWordToFailed(String wordBeingQuizd) {
		try {
			Writer output = new BufferedWriter(new FileWriter(FAILED, true));
			output.write(wordBeingQuizd+"\n");
			output.close();
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}	
	}
	
	protected void addWordToQuizd(String wordBeingQuizd) {
		try {
			Writer output = new BufferedWriter(new FileWriter(QUIZD, true));
			if (!wordExists(wordBeingQuizd, QUIZD)) {
				output.write(wordBeingQuizd+"\n");
				output.close();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	// Adds word to text file containing all words to be reviewed
	// as well as adding it to the arraylist containing the review words
	protected void addWordToReview(String wordBeingQuizd) {
		try {
			if (!reviewList.contains(wordBeingQuizd)) {
				reviewList.add(wordBeingQuizd);
			}
			Writer output = new BufferedWriter(new FileWriter(REVIEW, true));
			if (!wordExists(wordBeingQuizd, REVIEW)) {
				output.write(wordBeingQuizd+"\n");
				output.close();
			}
		} catch (IOException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	protected boolean wordExists(String wordBeingQuizd, String fileName) {
		File f = new File(fileName);
		try {
		    Scanner sc = new Scanner(f);
		    
		    while (sc.hasNextLine()) {
		        String line = sc.nextLine();
		        if (line.equals(wordBeingQuizd)) {
		        	sc.close();
		        	return true;
		        }
		        
		    }
		    sc.close();
		} catch(FileNotFoundException e) { 
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		
		return false;
	}
	
	protected int maxNumberOfWordsToSpell() {
		File review = new File(REVIEW);
		try {
			Scanner sc = new Scanner(new FileReader(review));
			int count = 0;
			while (sc.hasNextLine()) {
				count++;
				sc.nextLine();
			}
			sc.close();
			if (count == 1) {
				return 1;
			} else if (count == 2) {
				return 2;
			} else if (count >= 3) {
				return 3;
			}
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		return -1;
	}
	
	protected void removeFromReview(String wordToBeRemoved) {
		reviewList.remove(wordToBeRemoved);
		File review = new File(REVIEW);
		File temp = new File(TEMP);
		try {
			/* Following code retrieved and slightly modified from 
			 * http://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it */
			BufferedReader reader = new BufferedReader(new FileReader(review));
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
		
			String currentLine;

			while((currentLine = reader.readLine()) != null) {
		    
				String trimmedLine = currentLine.trim();
				if(trimmedLine.equals(wordToBeRemoved)) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close(); 
			reader.close(); 
			temp.renameTo(review);
			
		} catch (Exception e) { 
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	protected void clearStats() {
		/* Code retrieved and modified from http://stackoverflow.com/questions/6994518/how-to-delete-the-content-of-text-file-without-deleting-itself */
		try {
			PrintWriter m = new PrintWriter(MASTERED);
			m.print("");
			m.close();
			PrintWriter fau = new PrintWriter(FAULTED);
			fau.print("");
			fau.close();
			PrintWriter fai = new PrintWriter(FAILED);
			fai.print("");
			fai.close();
			PrintWriter q = new PrintWriter(QUIZD);
			q.print("");
			q.close();
			PrintWriter r = new PrintWriter(REVIEW);
			r.print("");
			r.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
	protected int numberOfTimesMastered(String wordToFind) {
		int count = 0;
		try {
			Scanner sc = new Scanner(new FileReader(MASTERED));
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.equals(wordToFind)) {
					count++;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		return count;
	}
	
	protected int numberOfTimesFaulted(String wordToFind) {
		int count = 0;
		try {
			Scanner sc = new Scanner(new FileReader(FAULTED));
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.equals(wordToFind)) {
					count++;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		return count;
	}
	
	protected int numberOfTimesFailed(String wordToFind) {
		int count = 0;
		try {
			Scanner sc = new Scanner(new FileReader(FAILED));
			String word;
			while (sc.hasNextLine()) {
				word = sc.nextLine();
				if (word.equals(wordToFind)) {
					count++;
				}
			}
			sc.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(frame, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
		return count;
	}
	
	private boolean isAWord(String word) {
		return word.matches("[a-zA-Z]+");
	}
	
}
