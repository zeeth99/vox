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

import voxspell.cards.ModeSelect;
import voxspell.cards.Menu;
import voxspell.cards.Quiz;
import voxspell.cards.Settings;
import voxspell.cards.Stats;

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

	final public static File WORDLIST = new File("NZCER-spelling-lists.txt");
	final public static File REVIEWLIST = new File(".history/failed");

	private CardLayout layout = new CardLayout();
	private JPanel cards = new JPanel();

	// Cards
	private Menu menu = new Menu(this);
	private Quiz quiz = new Quiz(this);
	private ModeSelect modeSelect = new ModeSelect(this);
	private Settings settings = new Settings(this);

	private SpellingAid(String[] args) throws FileNotFoundException {
		setResizable(false);
		setTitle("Spelling Aid");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		createReviewFiles();
		
		cards.setLayout(layout);
		
		// Set up cards
		cards.add(menu, "Menu");
		cards.add(modeSelect, "Level Select");
		cards.add(quiz, "Quiz");
		cards.add(settings, "Settings");

		getContentPane().add(cards);
		returnToMenu();

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == ((Menu)menu).newSpellingQuiz) {
			quiz.setReviewMode(false);
			layout.show(cards, "Level Select");
		} else if (e.getSource() == ((Menu)menu).reviewMistakes) {
			if (!reviewFilesEmpty()) {
				quiz.setReviewMode(true);
				startQuiz(1);
			} else {
				JOptionPane.showMessageDialog(this, "There are no words to revise.\nWell done!", "Nothing To Revise", JOptionPane.PLAIN_MESSAGE);
			}
		} else if (e.getSource() == ((Menu)menu).viewStatistics) {
			try {
				cards.add(new Stats(this), "Stats");
				layout.show(cards, "Stats");
			} catch (FileNotFoundException e1) {
				createStatsFiles();
			}
		} else if (e.getSource() == ((Menu)menu).clearStatistics) {
			clearStats();
		}
	}

	public void updateStats(String type, String word) {
		try {
			String[] files = {"mastered", "faulted", "failed"};
			File inputFile;
			File tempFile;
			BufferedReader reader;
			BufferedWriter writer;
			String currentLine;

			for (int i = 0; i < 3; i++) {
				String fileName = files[i];
				inputFile = new File(".history/"+fileName);
				tempFile = new File(".history/.tempFile");

				reader = new BufferedReader(new FileReader(inputFile));
				writer = new BufferedWriter(new FileWriter(tempFile));

				while((currentLine = reader.readLine()) != null) {
				    String trimmedLine = currentLine.trim();
				    if(trimmedLine.equals(word)) continue;
				    writer.write(currentLine + System.getProperty("line.separator"));
				}
				if (fileName.equals(type)) {
					writer.write(word + System.getProperty("line.separator"));
				}
				writer.close();
				reader.close();
				tempFile.renameTo(inputFile);
			}
			boolean wordFoundInAll = false;
			inputFile = new File(".history/all");
			tempFile = new File(".history/.tempFile");

			reader = new BufferedReader(new FileReader(inputFile));
			writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					if (type.equals("mastered")) {
						brokenLine[1] = "" + (Integer.parseInt(brokenLine[1]) + 1);
					} else if (type.equals("faulted")) {
						brokenLine[2] = "" + (Integer.parseInt(brokenLine[2]) + 1);
					} else {
						brokenLine[3] = "" + (Integer.parseInt(brokenLine[3]) + 1);
					}
					writer.write(brokenLine[0] + " " + brokenLine[1] + " " + brokenLine[2] + " " + brokenLine[3] + System.getProperty("line.separator"));
					wordFoundInAll = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFoundInAll) {
				if (type.equals("mastered")) {
					writer.write(word + " 1 0 0" + System.getProperty("line.separator"));
				} else if (type.equals("faulted")) {
					writer.write(word + " 0 1 0" + System.getProperty("line.separator"));
				} else {
					writer.write(word + " 0 0 1" + System.getProperty("line.separator"));
				}
			}
			tempFile.renameTo(inputFile);
			reader.close();
			writer.close();

			//updateStatsTable(type, word);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void clearStats() {
		JFrame popupFrame = new JFrame();
		String message = "This will permanently delete all of your spelling history.\n"
				+ "Are you sure you want to do this?";
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			String[] historyFileList = {"mastered", "faulted", "failed", "all"};
			for (int i = 0; i < 4; i++) (new File(".history/" + historyFileList[i])).delete();
			createStatsFiles();
		}
	}

	private static void createStatsFiles() {
		// Initialise .history
		File f = new File(".history");
		if (!f.exists() || !f.isDirectory()) {
			f.mkdir();
		}
		String[] historyFileList = {"mastered", "faulted", "failed", "all"};
		for (int i = 0; i < 4; i++) {
			f = new File(".history/" + historyFileList[i]);
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static void createReviewFiles() {
		// Creates files which contain words from each level which user failed to be
		// reviewed in review mode
		File r = new File(".history/review");
		if (!r.exists() || !r.isDirectory()) {
			r.mkdir();
		}
		for (int i = 1; i < 12; i++) {
			r = new File(".history/review/level"+i);
			try {
				r.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {

		createStatsFiles();

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

	public void startQuiz(int level) {
		layout.show(cards, "Quiz");
		quiz.startQuiz(level);
	}
	
	public void returnToMenu() {
		layout.show(cards, "Menu");
	}
	
	private boolean reviewFilesEmpty() {
		for (int i = 1; i < 12; i++) {
			File f = new File(".history/review/level"+i);
			if (f.length() > 0) {
				return false;
			}
		}
		return true;
	}

}
