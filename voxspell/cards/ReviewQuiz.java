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

	private boolean noWordsToReview() {
		if (_testingWords.size() == 0) {
			JOptionPane.showMessageDialog(this, "There are no words to review on this level");
			JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
			spellingAid.returnToMenu();
			return true;
		}
		return false;
	}

	protected void levelCompleteAction() {
		File f = new File(".history/"+_wordlist+".review");
		if (f.length() > 0) {
			String[] options = new String[] {"Repeat level","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "There are still some words left to review on this level\nTo progress to further levels"
					+ ", all words on this level must be cleared", "But wait.. there's more", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				startQuiz(_wordlist);
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
			spellingAid.returnToMenu();
		}
	}

}
