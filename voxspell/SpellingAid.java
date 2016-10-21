package voxspell;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import voxspell.quiz.CategorySelect;
import voxspell.quiz.Festival;
import voxspell.quiz.Quiz;
import voxspell.quiz.ReviewQuiz;
import voxspell.quiz.ReviewSelect;
import voxspell.quiz.WordList;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JInternalFrame;

/**
 * Main class for VOXSPELL. Handles the frame.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings({ "serial", "unused" })
public class SpellingAid extends JFrame implements ActionListener {

	private CardLayout layout = new CardLayout();
	private JPanel cards = new JPanel();

	// Cards
	private Menu menu;
	private Settings settings;
	private CategorySelect categorySelect;
	private ReviewSelect reviewSelect;
	private Quiz quiz;
	private Stats stats;

	/**
	 * Set up the program.
	 * @param args
	 */
	private SpellingAid(String[] args) {
		// Set LookAndFeel to Nimbus. If Nimbus isn't installed, set Look and Feel to
		// system Look and Feel.
		String LookAndFeel = UIManager.getSystemLookAndFeelClassName();
		for (LookAndFeelInfo LaF : UIManager.getInstalledLookAndFeels())
			if (LaF.getName().equals("Nimbus"))
				LookAndFeel = LaF.getClassName();
		try {
			UIManager.setLookAndFeel(LookAndFeel);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {}
		
		setResizable(false);
		setTitle("VOXSPELL");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Set up important files
		FileAccess.createFiles();

		cards.setLayout(layout);

		// Set up cards
		categorySelect = new CategorySelect(this);
		reviewSelect = new ReviewSelect(this);
		quiz = new Quiz(null);

		menu = new Menu(this);
		settings = new Settings(this);
		stats = new Stats(this);

		addCard(menu);
		addCard(settings);
		addCard(stats);

		setContentPane(cards);
		returnToMenu();
		setVisible(true);
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

	/**
	 * Change view to specified Card
	 * @param c - The Card to view
	 */
	private void viewCard(Card c) {
		layout.show(cards, c.cardName());
	}

	/**
	 * Add a Card to be able to view.
	 * @param c - The Card to add to the program
	 */
	private void addCard(Card c) {
		cards.add(c, c.cardName());
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
			layout.show(cards, menu.toString());
			new ErrorMessage(e);
		}
	}

	/**
	 * View the menu screen.
	 */
	public void returnToMenu() {
		viewCard(menu);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SpellingAid(args);
			}
		});
	}
}
