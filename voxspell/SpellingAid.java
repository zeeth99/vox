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
 * TODO document this class
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
	private Quiz quiz;

	private SpellingAid(String[] args) throws FileNotFoundException {
		setResizable(false);
		setTitle("VOXSPELL");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Set up important files
		FileAccess.createFiles();

		cards.setLayout(layout);

		// Set up cards
		menu = new Menu(this);
		addCard(menu);
		settings = new Settings(this);
		addCard(settings);
		quiz = new Quiz(null);
		addCard(quiz);

		setContentPane(cards);
		returnToMenu();
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menu.settings) {
			// Settings
			viewCard(settings);
			return;
		}
		Card c = null;
		if (e.getSource() == menu.newSpellingQuiz) {
			// New Quiz
			c = new CategorySelect(this);
			quiz = new Quiz(this);
			addCard(quiz);
		} else if (e.getSource() == menu.reviewQuiz) {
			// Review Quiz
			c = new ReviewSelect(this);
			quiz = new ReviewQuiz(this);
			addCard(quiz);
		} else if (e.getSource() == menu.viewStatistics) {
			// Statistics
			c = new Stats(this);
		}
		addCard(c);
		viewCard(c);
	}

	private void viewCard(Card c) {
		layout.show(cards, c.cardName());
	}

	private void addCard(Card c) {
		cards.add(c, c.cardName());
	}

	public void startQuiz(WordList w) {
		try {
			w.setup();
			viewCard(quiz);
			quiz.startQuiz(w);
		} catch (FileNotFoundException e) {
			String message = "The file containing that category has been removed from "+FileAccess.WORDFOLDER;
			JOptionPane.showMessageDialog(new JFrame(), message, "File Not Found", JOptionPane.ERROR_MESSAGE);
			layout.show(cards, menu.toString());
		}
	}

	public void returnToMenu() {
		viewCard(menu);
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				try {
					new SpellingAid(args);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
