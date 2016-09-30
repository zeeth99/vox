package voxspell.cards;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;
import voxspell.SpellingAid.QuizResult;
import voxspell.Wordlist;
import voxspell.BestMediaPlayer;
import voxspell.Festival;

@SuppressWarnings("serial")
public class ReviewQuiz extends Quiz {

	private static final int QUIZ_SIZE = 10;

	private boolean _reviewSpellOut;

	public ReviewQuiz(SpellingAid sp) {
		super(sp);
	}

	private void checkWord() {
		String input = inputBox.getText();
		String word = _testingWords.get(_wordNumber);
		inputBox.setText("");

		String festivalMessage;

		if (_reviewSpellOut) { // Check if word is spelled correctly on their last chance
			_reviewSpellOut = false;
			if (input.equalsIgnoreCase(word)) {
				festivalMessage = "correct:";
			} else {
				festivalMessage = "incorrect:";
				addWordToReview(word, _category);
			}
		} else if (_firstAttempt) {
			if (input.equalsIgnoreCase(word)) {
				// MASTERED
				((SpellingAid) spellingAid).updateStats(QuizResult.MASTERED, word, _category);
				festivalMessage = "correct:";
				_wordsCorrect++;
				removeFromReview(word);
			} else {
				// FIRST FAIL
				_firstAttempt = false;
				sayMessage("Incorrect: The word is " + _testingWords.get(_wordNumber) + ":.:" + _testingWords.get(_wordNumber));
				return;
			}
		} else {
			if (input.equalsIgnoreCase(word)) {
				// FAULTED
				((SpellingAid) spellingAid).updateStats(QuizResult.FAULTED, word, _category);
				festivalMessage = "correct:";
				_wordsCorrect++;
				removeFromReview(word);
			} else {
				// FAILED
				((SpellingAid) spellingAid).updateStats(QuizResult.FAILED, word, _category);
				festivalMessage = "incorrect:";
				if (_reviewMode && !_reviewSpellOut) {
					_reviewSpellOut = true;
					festivalMessage += "the word is spelt:";
					// Spell out word if reviewing
					String spellOutWord = "";
					for (int i = 0; i < word.length(); i++) {
						spellOutWord += word.charAt(i) + ": ";
					}
					festivalMessage += spellOutWord;
					sayMessage(festivalMessage);
					return;
				}
				addWordToReview(word, _category);
			}
		}

		_firstAttempt = true;
		_wordNumber++;
		feedbackPanel.setText(_wordsCorrect + " out of " + _wordNumber + " correct so far");

		// If all words have been tested:
		if (_wordNumber == _testingWords.size()) {
			sayMessage(festivalMessage);
			if (_reviewMode) {
				reviewLevelCompleteAction();
			} else {
				// Check to see if user has completed a level i.e. has gotten 9 out of 10 words correct
				if (_wordsCorrect >= 9) {
					levelCompleteAction();
				} else {
					levelIncompleteAction();
				}
			}
		} else {
			// Test next word
			sayMessage(festivalMessage+"Please spell "+_testingWords.get(_wordNumber));
			wordCountLabel.setText("Word " + (_wordNumber+1) +" of " + _testingWords.size());
		}
	}
	
	protected void quizHook() {
		heading.setText("Review Quiz");
		_testingWords = _wordlist.reviewWords(QUIZ_SIZE);
		_reviewSpellOut = false;
	}

	/* Decides on what to do when the level is completed depending on what the user
	 * chooses to do and what level they are on [FOR NORMAL QUIZ ONLY]
	 */
	public void levelCompleteAction() {
		_wordNumber = 0;
		_wordsCorrect = 0;
		if (_category != 11) {
			// Give option for video reward before asking to progress to next level
			String[] options = new String[] {"Next Level","Repeat Level","Play Video", "Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "You have completed this level!\nWhat would you like to do?", "Congratulations!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_category + 1);
			} else if (option == 1) {
				spellingAid.startQuiz(_category);
			} else if (option == 2) {
				selectFilterAndPlay();
				spellingAid.returnToMenu();
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			String[] options = new String[] {"Repeat Level","Play Video","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "You have completed this level!\nWhat would you like to do?", "Congratulations!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_category);
			} else if (option == 1) {
				selectFilterAndPlay();
				spellingAid.returnToMenu();
			} else {
				spellingAid.returnToMenu();
			}
		}
	}

	/* Decides on what to do when the level is not completed [FOR NORMAL QUIZ ONLY]
	 */
	public void levelIncompleteAction() {
		String[] options = new String[] {"Repeat level","Return to Main Menu"};
		int option = JOptionPane.showOptionDialog(this, "You didn't complete the level.\nTo complete a level, you must get 9 out of the 10 words correct. What would you like to do?",
				"Unfortunate my friend", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == JOptionPane.YES_OPTION) {
			spellingAid.startQuiz(_category);
		} else {
			spellingAid.returnToMenu();
		}
	}

	private List<String> randomWords(File f, int level) {
		List<String> tempList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
			if (!_reviewMode) {
				while (sc.hasNextLine()) {
					if (sc.nextLine().equals("%Level "+level)) {
						break;
					}
				}
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (!_reviewMode) {
					if (line.charAt(0) == '%') {
						break;
					}
				}
				tempList.add(line);
			}
			sc.close();
		} catch (FileNotFoundException e) { }
		Random rnd = new Random();
		for (int i = 0; i < QUIZ_SIZE; i++) {
			if (tempList.isEmpty()) {
				break;
			}
			String word = tempList.get(rnd.nextInt(tempList.size()));
			tempList.remove(word);
			wordList.add(word);
		}
		return wordList;
	}

	private void addWordToReview(String word, int level) {
		try {	
			String currentLine;
			File inputFile = new File(".history/level"+level+"/toReview");
			File tempFile = new File(".history/.tempFile");

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));

			while((currentLine = reader.readLine()) != null) {
				String trimmedLine = currentLine.trim();
				if (trimmedLine.equals(word)) continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.write(word + System.getProperty("line.separator"));
			writer.close();
			reader.close();
			tempFile.renameTo(inputFile);
		} catch (Exception e) { }
	}

	private boolean noWordsToReview() {
		if (_testingWords.size() == 0) {
			wordCountLabel.setText("");
			if (_category != 11) {
				String[] options = new String[] {"Continue", "Return to Main Menu"};
				int option = JOptionPane.showOptionDialog(this, "There are no words to review on this level",
						"Select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					spellingAid.startQuiz(_category+1);
				} else {
					spellingAid.returnToMenu();
				}
			} else {
				JOptionPane.showMessageDialog(this, "There are no words to review on this level");
				JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
				spellingAid.returnToMenu();
			}
			return true;
		}
		return false;
	}

	private void reviewLevelCompleteAction() {
		File f = new File(".history/level"+_category+"/toReview");
		if (f.length() > 0) {
			String[] options = new String[] {"Repeat level","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "There are still some words left to review on this level\nTo progress to further levels"
					+ ", all words on this level must be cleared", "But wait.. there's more", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_category);
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			if (_category != 11) {
				String[] options = new String[] {"Continue","Return to Main Menu"};
				int option = JOptionPane.showOptionDialog(this, "You have cleared all words on this level", "Congratulations!", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					spellingAid.startQuiz(_category + 1);
				} else {
					spellingAid.returnToMenu();
				}
			} else {
				JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
				spellingAid.returnToMenu();
			}
		}
	}

	private void removeFromReview(String wordToBeRemoved) {
		File review = new File(".history/"+_category+"/toReview");
		File temp = new File(".history/.tempFile");
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
			JOptionPane.showMessageDialog(this, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}

}
