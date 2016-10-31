package voxspell.quiz.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JScrollPane;

import voxspell.Card;
import voxspell.CardManager;
import voxspell.quiz.QuizCard;
import voxspell.resource.SortedListModel;

@SuppressWarnings("serial")
public abstract class AbstractCategorySelect extends Card {

	public JButton startQuiz;
	protected JScrollPane scrollPane;
	protected RegularCategoryList list;
	
	protected QuizCard _quiz;
	
	/**
	 * Set up GUI
	 * @param cm - The CardManager that created this
	 */
	public AbstractCategorySelect(CardManager cm, QuizCard quiz) {
		super(cm, "Select Your WordList");
		_quiz = quiz;

		createList();
		list.addKeyListener(new KeyAdapter(){ // Use enter to start a quiz
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					startQuiz.doClick();
			}
		});
		setDefaultFocusComponent(list);

		// List of categories
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(12, 55, 476, 265);
		add(scrollPane);

		// Button to start quiz with currently selected category
		startQuiz = new JButton("Start Quiz");
		startQuiz.setBounds(383, 15, 105, 30);
		startQuiz.setToolTipText("Start a quiz with the selected category.");
		startQuiz.addActionListener(getCardManager());
		startQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setReviewMode();
				_quiz.quizzer.setWordlist(list.getSelectedValue());
			}
		});
		startQuiz.setFocusable(false);
		add(startQuiz);

	}

	protected abstract void setReviewMode();

	protected abstract void createList();

	@Override
	public void onCardShown() {
		list.setupListModel();
	}

	public void onCardHidden() {
		((SortedListModel<?>) list.getModel()).clear();
	}

	public String cardName() {
		return "Category Select";
	}
}
