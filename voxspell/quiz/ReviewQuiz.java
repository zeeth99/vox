package voxspell.quiz;

import voxspell.CardManager;

/**
 * Screen to test the user on words they answered incorrectly in a category.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class ReviewQuiz extends QuizCard {

	/**
	 * Set up GUI.
	 * @param cm - The CardManager that created this
	 */
	public ReviewQuiz(CardManager cm) {
		super(cm);
		heading.setText("Review Quiz");
	}

}
