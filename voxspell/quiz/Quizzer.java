package voxspell.quiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.swing.JOptionPane;

import voxspell.BestMediaPlayer;
import voxspell.ErrorMessage;
import voxspell.FileAccess;
import voxspell.BestMediaPlayer.Video;

public class Quizzer implements ActionListener {
	
	private static final int QUIZ_SIZE = 10;
	
	private QuizCard _quiz;
	
	protected boolean _firstAttempt;
	protected int _wordNumber;
	private int _wordsCorrect;
	protected List<String> _testingWords;

	protected WordList _wordlist;
	
	public Quizzer(QuizCard quiz) {
		_quiz = quiz;
	}


	/**
	 * Starts the quiz with the specified WordList.
	 * @param w - The {@link WordList} to test the user on
	 */
	public void startQuiz() {
		try {
			_wordlist.setup();
		} catch (FileNotFoundException e) {
			new ErrorMessage(e);
			_quiz.getCardManager().returnToMenu();
		}
		_wordNumber = 0;
		_wordsCorrect = 0;

		_testingWords = _wordlist.randomWords(QUIZ_SIZE);
		
		_quiz.start(_wordlist.category(), _testingWords.size());

		if (_testingWords.size() == 0) {
			_quiz.getCardManager().returnToMenu();
			new ErrorMessage("The category chosen is empty.\nYou will be returned to the menu.", "Empty Quiz");
			return;
		}

		_firstAttempt = true;

		sayMessage("Please spell " + _testingWords.get(_wordNumber));
	}

	/**
	 * Checks if the word was spelled correctly.
	 * Gives order to update stats based on successfulness.
	 * Updates GUI based on progress.
	 * Gives order to perform appropriate level completion action.
	 * @param input
	 */
	protected void checkWord(String input) {
		String word = _testingWords.get(_wordNumber);
		String festivalMessage;

		boolean correct = input.equalsIgnoreCase(word);

		_quiz.clearInputBox();
		
		if (correct) {
			festivalMessage = "Correct:";
			_wordsCorrect++;
		} else {
			festivalMessage = "Incorrect:";
			if (_firstAttempt) {
				// FIRST FAIL
				_firstAttempt = false;
				sayMessage(festivalMessage+"The word is " + word + ":.:" + word);
				
				return;
			}
		}
		_quiz.update(correct);
		FileAccess.updateStats(correct, word, _wordlist);

		_firstAttempt = true;
		_wordNumber++;

		// If all words have been tested:
		if (_wordNumber == _testingWords.size()) {
			sayMessage(festivalMessage);
			endQuiz();
		} else {
			// Test next word
			sayMessage(festivalMessage + "Spell " + _testingWords.get(_wordNumber));
		}
	}

	/**
	 * Decide what to do on quiz end.
	 */
	protected void endQuiz() {
		// If the word list is 1 or 2 words long, the user needs to get full marks to 'pass'
		// Otherwise they need to get at most one word wrong.
		String[] options = {"Repeat Level", "Play Victory Video", "Return to Main Menu"};
		String message;
		String heading;
		if ((_wordsCorrect == _testingWords.size()-1 && _testingWords.size() > 2)
				|| _wordsCorrect == _testingWords.size()) {
			message = "You have completed this level!\nWhat would you like to do?";
			heading = "Congratulations!";
		} else {
			message = "You didn't complete the level.\nTo complete a level, you must get at most one word incorrect. "
					+ "What would you like to do?";
			heading = "Unfortunate my friend";
			options = new String[] {options[0], options[2]};
		}
		endQuizOptions(message, heading, options, options.length-1);
	}

	/**
	 * Gives user options as level ends.
	 * @param message - the message to display in the JOptionPane
	 * @param heading - the heading to display in the JOptionPane
	 * @param options - the options to display in the JOptionPane
	 * @param menuOption - index of the option which returns the user to the menu
	 */
	protected void endQuizOptions(String message, String heading, String[] options, int menuOption) {
		// Give option for video reward.
		int option = JOptionPane.showOptionDialog(null, message, heading, JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == menuOption || option == JOptionPane.CLOSED_OPTION){
			_quiz.getCardManager().returnToMenu();
		} else if (option == 0) {
			startQuiz();
		} else {
			selectFilterAndPlay();
			options = new String[] {options[0], options[2]};
			endQuizOptions(message, heading, options, 1);
		}
	}
	
	/**
	 * Makes a text to speech call with Festival.
	 * @param message - A string representing the message for Festival to say
	 */
	protected void sayMessage(String message) {
		_quiz.setButtonsEnabled(false);
		(new Festival(message, this)).execute();
	}
	
	public void enableButtons() {
		_quiz.setButtonsEnabled(true);
	}

	/**
	 * Gives user choice of videos filter, then plays video reward.
	 */
	private static void selectFilterAndPlay() {
		if (!new File(Video.NORMAL.toString()).exists()) {
			JOptionPane.showMessageDialog(null, Video.NORMAL+" does not exist within "+ClassLoader.getSystemClassLoader().getResource(".").getPath()+"\n"
					+ "If you ran VOXSPELL without using the runVoxspell.sh script, then "+Video.NORMAL+" doesn't exist within your home directory\n"
					+ "Please make sure this file exists in the correct directory as to enable the video to be played");
			return;
		}
		String[] options = new String[] {"Normal","Negative"};
		int option = JOptionPane.showOptionDialog(null, "Select a filter", "Video Filter",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			new BestMediaPlayer(Video.NORMAL);
		} else {
			new BestMediaPlayer(Video.NEGATIVE);
		}
	}

	public void setWordlist(WordList w) {
		_wordlist = w;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == _quiz.getRepeatButton())
			sayMessage(_testingWords.get(_wordNumber));
		if (source == _quiz.getSubmitButton())
			checkWord(_quiz.inputBox.getText());
	}

}
