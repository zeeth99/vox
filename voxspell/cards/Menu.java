package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Menu extends JPanel implements ActionListener {

	private JLabel menuLabel;

	public JButton newSpellingQuiz;
	public JButton reviewMistakes;
	public JButton viewStatistics;
	public JButton clearStatistics;

	
	public Menu(SpellingAid sp) {
		menuLabel = new JLabel("Welcome to the Spelling Aid");
		menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
		menuLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		menuLabel.setBounds(0, 0, 500, 60);
		newSpellingQuiz = new JButton("New Spelling Quiz");
		newSpellingQuiz.setFont(new Font("Dialog", Font.BOLD, 16));
		newSpellingQuiz.setBounds(100, 90, 300, 50);
		newSpellingQuiz.addActionListener((ActionListener) sp);
		reviewMistakes = new JButton("Review Mistakes");
		reviewMistakes.setFont(new Font("Dialog", Font.BOLD, 16));
		reviewMistakes.setBounds(100, 150, 300, 50);
		reviewMistakes.addActionListener((ActionListener) sp);
		viewStatistics = new JButton("View Statistics");
		viewStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		viewStatistics.setBounds(100, 210, 300, 50);
		viewStatistics.addActionListener(sp);
		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.addActionListener(sp);

		setLayout(null);
		add(menuLabel);
		add(reviewMistakes);
		add(viewStatistics);
		add(clearStatistics);
		add(newSpellingQuiz);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
