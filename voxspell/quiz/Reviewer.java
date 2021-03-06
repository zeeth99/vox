package voxspell.quiz;

import javax.swing.JOptionPane;

public class Reviewer extends Quizzer {


	public Reviewer(QuizCard quiz) {
		super(quiz);
	}

	/**
	 * Shows the user how to spell the word if they spelled it wrong twice.
	 * @param input - The attempted spelling of the word.
	 */
	protected void checkWord(String input) {
		String word = _testingWords.get(_wordNumber);
		if (!input.equalsIgnoreCase(word) && !_firstAttempt)
			JOptionPane.showMessageDialog(null, "The word was: "+word, "\""+word+"\"", JOptionPane.PLAIN_MESSAGE);
		super.checkWord(input);
	}

	protected void endQuiz() {
		// If the word list is 1 or 2 words long, the user needs to get full marks to 'pass'
		// Otherwise they need to get at most one word wrong.
		String[] options = {"Return to Main Menu"};
		String message = "You have finished this review Quiz.";
		String heading = "End of Review Quiz";
		endQuizOptions(message, heading, options, options.length-1);
	}

}
