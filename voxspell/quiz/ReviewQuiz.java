package voxspell.quiz;

import java.io.File;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ReviewQuiz extends Quiz {

	/**
	 * 
	 * @param sp - The SpellingAid that created this
	 */
	public ReviewQuiz(SpellingAid sp) {
		super(sp);
		heading.setText("Review Quiz");
	}

	protected void checkWord(String input) {
		if (!input.equalsIgnoreCase(_testingWords.get(_wordNumber)) && !_firstAttempt) {
			// spell it out? 
			// write it out?
			// both?
		}
		super.checkWord(input);
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
