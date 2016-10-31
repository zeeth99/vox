package voxspell.quiz.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import voxspell.CardManager;
import voxspell.quiz.QuizCard;

import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class ReviewCategorySelect extends AbstractCategorySelect {

	/**
	 * Set up GUI.
	 * @param cm - The CardManager that created this
	 */
	public ReviewCategorySelect(CardManager cm, QuizCard quiz) {
		super(cm, quiz);

		// The following buttons dictate the number of stars a word can have to be considered reviewable.
		JToggleButton twoStar = new JToggleButton("Least known words");
		twoStar.setName("2");
		twoStar.setBounds(12,320,238,30);
		twoStar.setToolTipText("Change review mode word criterion.");
		twoStar.setFocusable(false);

		JToggleButton threeStar = new JToggleButton("All words without three stars");
		threeStar.setName("3");
		threeStar.setBounds(250,320,238,30);
		threeStar.setToolTipText("Change review mode word criterion.");
		threeStar.setFocusable(false);

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				twoStar.setSelected(false);
				threeStar.setSelected(false);
				((JToggleButton) e.getSource()).setSelected(true);
			}
		};

		twoStar.addActionListener(listener);
		threeStar.addActionListener(listener);
		twoStar.addActionListener((ActionListener) list);
		threeStar.addActionListener((ActionListener) list);

		add(twoStar);
		add(threeStar);
		twoStar.doClick();
	}

	@Override
	protected void createList() {
		list = new ReviewCategoryList();
	}

	@Override
	protected void setReviewMode() {
		_quiz.setReviewMode(true);
	}
	
}

