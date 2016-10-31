package voxspell;

import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import voxspell.quiz.QuizCard;
import voxspell.quiz.start.RegularCategorySelect;
import voxspell.quiz.start.ReviewCategorySelect;
import voxspell.stats.Stats;

@SuppressWarnings("serial")
public class CardManager extends JPanel implements ActionListener {

	// Cards
	private Menu menu;
	private Settings settings;
	private RegularCategorySelect regularCategorySelect;
	private ReviewCategorySelect reviewCategorySelect;
	private QuizCard quiz;
	private Stats stats;
	
	public CardManager() {
		setLayout(new CardLayout());
		// Set up cards
		menu = new Menu(this);
		settings = new Settings(this);
		stats = new Stats(this);
		quiz = new QuizCard(this);
		
		regularCategorySelect = new RegularCategorySelect(this, quiz);
		reviewCategorySelect = new ReviewCategorySelect(this, quiz);

		addCard(menu);
		addCard(settings);
		addCard(stats);
		addCard(quiz);
		
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
		if (source == regularCategorySelect.startQuiz || source == reviewCategorySelect.startQuiz) {
			viewCard(quiz);
		}
		// New Quiz
		if (source == menu.newSpellingQuiz) {
			addCard(regularCategorySelect);
			viewCard(regularCategorySelect);
		}
		// Review Quiz
		if (source == menu.reviewQuiz) {
			addCard(reviewCategorySelect);
			viewCard(reviewCategorySelect);
		}
		// Statistics
		if (source == menu.viewStatistics)
			viewCard(stats);
		// Settings
		if (source == menu.settings)
			viewCard(settings);
	}

}
