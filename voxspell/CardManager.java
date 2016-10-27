package voxspell;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import voxspell.quiz.CategorySelect;
import voxspell.quiz.QuizCard;
import voxspell.quiz.ReviewQuiz;
import voxspell.quiz.ReviewSelect;
import voxspell.stats.Stats;

@SuppressWarnings("serial")
public class CardManager extends JPanel implements ActionListener {

	// Cards
	private Menu menu;
	private Settings settings;
	private CategorySelect categorySelect;
	private ReviewSelect reviewSelect;
	private QuizCard quiz;
	private ReviewQuiz review;
	private Stats stats;
	
	public CardManager() {
		setLayout(new CardLayout());
		// Set up cards
		quiz = new QuizCard(this);
		review = new ReviewQuiz(this);
		categorySelect = new CategorySelect(this, quiz);
		reviewSelect = new ReviewSelect(this, review);

		menu = new Menu(this);
		settings = new Settings(this);
		stats = new Stats(this);

		addCard(menu);
		addCard(settings);
		addCard(stats);
		
		returnToMenu();
	}

	/**
	 * Change view to specified Card
	 * @param c - The Card to view
	 */
	public void viewCard(Card c) {
		((CardLayout) getLayout()).show(this, c.cardName());
	}

	/**
	 * Add a Card to be able to view.
	 * @param c - The Card to add to the program
	 */
	public void addCard(Card c) {
		add(c, c.cardName());
	}

	/**
	 * View the menu screen.
	 */
	public void returnToMenu() {
		viewCard(menu);
	}

	/**
	 * Display one a screen depending on what button was pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// Start Quiz
		if (source == categorySelect.startQuiz) {
			viewCard(quiz);
		}
		// New Quiz
		if (source == menu.newSpellingQuiz) {
			addCard(quiz);
			addCard(categorySelect);
			viewCard(categorySelect);
		}
		// Review Quiz
		if (source == menu.reviewQuiz) {
			addCard(review);
			addCard(reviewSelect);
			viewCard(reviewSelect);
		}
		// Statistics
		if (source == menu.viewStatistics)
			viewCard(stats);
		// Settings
		if (source == menu.settings)
			viewCard(settings);
	}

}
