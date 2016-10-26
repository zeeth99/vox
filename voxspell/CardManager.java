package voxspell;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;

import javax.swing.JPanel;

import voxspell.quiz.CategorySelect;
import voxspell.quiz.Quiz;
import voxspell.quiz.ReviewQuiz;
import voxspell.quiz.ReviewSelect;
import voxspell.quiz.WordList;
import voxspell.stats.Stats;

@SuppressWarnings("serial")
public class CardManager extends JPanel implements ActionListener {

	// Cards
	private Menu menu;
	private Settings settings;
	private CategorySelect categorySelect;
	private ReviewSelect reviewSelect;
	private Quiz quiz;
	private Stats stats;
	
	public CardManager() {
		setLayout(new CardLayout());
		// Set up cards
		categorySelect = new CategorySelect(this);
		reviewSelect = new ReviewSelect(this);
		quiz = new Quiz(this);

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
	 * Start a Quiz with the specified WordList.
	 * @param w - The WordList be quizzed on
	 */
	public void startQuiz(WordList w) {
		try {
			w.setup();
			viewCard(quiz);
			quiz.startQuiz(w);
		} catch (FileNotFoundException e) {
			viewCard(menu);
			new ErrorMessage(e);
		}
	}

	/**
	 * Display one a screen depending on what button was pressed.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		// New Quiz
		if (source == menu.newSpellingQuiz) {
			quiz = new Quiz(this);
			addCard(quiz);
			addCard(categorySelect);
			viewCard(categorySelect);
		}
		// Review Quiz
		if (source == menu.reviewQuiz) {
			quiz = new ReviewQuiz(this);
			addCard(quiz);
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
