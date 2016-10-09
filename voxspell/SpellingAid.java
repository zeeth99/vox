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


@SuppressWarnings({ "serial", "unused" })
public class SpellingAid extends JFrame implements ActionListener {

	final public static File WORDFOLDER = new File("wordlists");
	final public static File STATSFOLDER = new File(".history");
	final public static File FESTIVALFOLDER = new File(".festival");

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
		createFiles();

		cards.setLayout(layout);

		// Set up cards
		menu = new Menu(this);
		addCard(menu);
		settings = new Settings(this);
		addCard(settings);
		quiz = new Quiz(null);
		addCard(quiz);

		getContentPane().add(cards);
		returnToMenu();

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menu.settings) {
			viewCard(settings);
			return;
		}
		Card c = null;
		if (e.getSource() == menu.newSpellingQuiz) {
			c = new CategorySelect(this);
			quiz = new Quiz(this);
			addCard(quiz);
		} else if (e.getSource() == menu.reviewQuiz) {
			c = new ReviewSelect(this);
			quiz = new ReviewQuiz(this);
			addCard(quiz);
		} else if (e.getSource() == menu.viewStatistics) {
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
	
	public void updateStats(boolean correct, String word, WordList w) {
		// stats files are stored in the following format:
		// {word} {number of times the word was successfully attempted} {number of times the word was attempted}
		try {
			boolean wordFound = false;
			File tempFile = new File(".history/.tempFile");
			File inputFile = new File(".history/"+w+".stats");
			inputFile.createNewFile();
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				// Update stats line with current word.
				if (!wordFound && currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					int timesCorrect = Integer.parseInt(brokenLine[1]);
					int timesAttempted = Integer.parseInt(brokenLine[2]) + 1;
					if (correct)
						timesCorrect++;
					writer.write(brokenLine[0]+" "+timesCorrect+" "+timesAttempted+System.getProperty("line.separator"));
					wordFound = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFound)
				if (correct) {
					writer.write(word + " 1 1" + System.getProperty("line.separator"));
				} else {
					writer.write(word + " 0 1" + System.getProperty("line.separator"));
				}
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		updateRecentStats(correct, word, w);
	}

	public void updateRecentStats(boolean correct, String word, WordList w) {
		// Recent stats are stored in the following format:
		// {word} {0|1} {0|1} {0|1}
		// 1 represents a successful attempt on the word, 0 represents a failed attempt
		// The rightmost number represents the most recent attempt. 
		try {
			boolean wordFound = false;
			File tempFile = new File(".history/.tempFile");
			File inputFile = new File(".history/"+w+".recent");
			inputFile.createNewFile();
			String currentLine;

			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				// Update recent stats line with the current word.
				if (!wordFound && currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					int i = 0;
					if (correct) 
						i = 1;
					writer.write(brokenLine[0] + " " + brokenLine[2] + " " + brokenLine[3] + " " + i + System.getProperty("line.separator"));
					wordFound = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFound) {
				if (correct) {
					writer.write(word + " 0 0 1" + System.getProperty("line.separator"));
				} else {
					writer.write(word + " 0 0 0" + System.getProperty("line.separator"));
				}
			}
			reader.close();
			writer.close();
			tempFile.renameTo(inputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void createFiles() {
		if (!WORDFOLDER.exists() || !WORDFOLDER.isDirectory())
			WORDFOLDER.mkdir();
		if (!STATSFOLDER.exists() || !STATSFOLDER.isDirectory())
			STATSFOLDER.mkdir();
		if (!FESTIVALFOLDER.exists() || !FESTIVALFOLDER.isDirectory())
			FESTIVALFOLDER.mkdir();
		try {
			new File(".festival/message.scm").createNewFile();
		} catch (IOException e) {}
		try {   
			List<String> linesToWrite = new ArrayList<>();
			linesToWrite.add(Settings.DEFAULT_VOICE);
			Files.write(Festival.SCHEME_FILE.toPath(), linesToWrite); 
		} catch (Exception e) {} 
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

	public void startQuiz(WordList w) {
		try {
			w.setup();
			viewCard(quiz);
			quiz.startQuiz(w);
		} catch (FileNotFoundException e) {
			String message = "The file containing that category has been removed from "+WORDFOLDER;
			JOptionPane.showMessageDialog(new JFrame(), message, "File Not Found", JOptionPane.ERROR_MESSAGE);
			layout.show(cards, menu.toString());
		}
	}

	public void returnToMenu() {
		viewCard(menu);
	}
}
