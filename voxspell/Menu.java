package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

/**
 * The main menu screen, containing buttons to go to different screens
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Menu extends Card implements ActionListener {

	public JButton newSpellingQuiz;
	public JButton viewStatistics;
	public JButton settings;
	public JButton reviewQuiz;

	/**
	 * Set up menu GUI
	 * @param sp - The SpellingAid that created this
	 */
	public Menu(SpellingAid sp) {
		super(sp, "Welcome to VOXSPELL");

		// This button is pointless on this screen.
		menuButton.setEnabled(false);
		menuButton.setVisible(false);

		// Button to start a new quiz
		newSpellingQuiz = new JButton("New Spelling Quiz");
		newSpellingQuiz.setFont(new Font("Dialog", Font.BOLD, 16));
		newSpellingQuiz.setBounds(100, 90, 300, 50);
		newSpellingQuiz.addActionListener((ActionListener) sp);
		
		// Button to review mistakes
		reviewQuiz = new JButton("Review Mistakes");
		reviewQuiz.setFont(new Font("Dialog", Font.BOLD, 16));
		reviewQuiz.setBounds(100, 150, 300, 50);
		reviewQuiz.addActionListener((ActionListener) sp);
		
		// Button to go to stats screen
		viewStatistics = new JButton("View Statistics");
		viewStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		viewStatistics.setBounds(100, 210, 300, 50);
		viewStatistics.addActionListener(sp);
		
		// Button to go to settings screen
		settings = new JButton("Settings");
		settings.setFont(new Font("Dialog", Font.BOLD, 16));
		settings.setBounds(100, 270, 300, 50);
		settings.addActionListener((ActionListener) sp);

		add(newSpellingQuiz);
		add(reviewQuiz);
		add(viewStatistics);
		add(settings);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {}

	public String cardName() {
		return "Menu";
	}
}
