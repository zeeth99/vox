package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Menu extends Card implements ActionListener {

	public JButton newSpellingQuiz;
	public JButton viewStatistics;
	public JButton settings;
	public JButton clearStatistics;

	public Menu(SpellingAid sp) {
		super(sp, "Welcome to the Spelling Aid");
		
		newSpellingQuiz = new JButton("New Spelling Quiz");
		newSpellingQuiz.setFont(new Font("Dialog", Font.BOLD, 16));
		newSpellingQuiz.setBounds(100, 90, 300, 50);
		newSpellingQuiz.addActionListener((ActionListener) sp);
		viewStatistics = new JButton("View Statistics");
		viewStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		viewStatistics.setBounds(100, 150, 300, 50);
		viewStatistics.addActionListener(sp);
		settings = new JButton("Settings");
		settings.setFont(new Font("Dialog", Font.BOLD, 16));
		settings.setBounds(100, 210, 300, 50);
		settings.addActionListener((ActionListener) sp);
		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.addActionListener(sp);

		add(newSpellingQuiz);
		add(viewStatistics);
		add(settings);
		add(clearStatistics);
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
