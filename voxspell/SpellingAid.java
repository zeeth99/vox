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

import voxspell.cards.CategorySelect;
import voxspell.cards.Menu;
import voxspell.cards.Quiz;
import voxspell.cards.ReviewSelect;
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

	public enum QuizResult {
		MASTERED(1), 
		FAULTED(2), 
		FAILED(3);
		
		public final int integerValue;
		
		private QuizResult(int integer) {
			integerValue = integer;
		}
	}
	
	final public static File WORDFOLDER = new File("wordlists");
	final public static File STATSFOLDER = new File(".history");
	final public static File FESTIVALFOLDER = new File(".festival");

	private CardLayout layout = new CardLayout();
	private JPanel cards = new JPanel();

	// Cards
	private Menu menu;
	private Quiz quiz;
	private Settings settings;

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
		cards.add(menu, "Menu");
		quiz = new Quiz(this);
		cards.add(quiz, "Quiz");
		settings = new Settings(this);
		cards.add(settings, "Settings");

		getContentPane().add(cards);
		returnToMenu();

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menu.newSpellingQuiz) {
			cards.add(new CategorySelect(this), "Category Select");
			layout.show(cards, "Category Select");
		} else if (e.getSource() == menu.reviewQuiz) {
			cards.add(new ReviewSelect(this), "Category Select");
			layout.show(cards, "Category Select");
		} else if (e.getSource() == menu.viewStatistics) {
			try {
				cards.add(new Stats(this), "Stats");
				layout.show(cards, "Stats");
			} catch (FileNotFoundException e1) {
				createFiles();
				try {
					cards.add(new Stats(this), "Stats");
					layout.show(cards, "Stats");
				} catch (FileNotFoundException e2) {
					e2.printStackTrace();
				}
			}
		} else if (e.getSource() == menu.settings) {
			layout.show(cards, "Settings");
		}
	}

	public void updateStats(QuizResult type, String word, WordList w) {
		try {
			File inputFile;
			File tempFile;
			BufferedReader reader;
			BufferedWriter writer;
			String currentLine;
			
			boolean wordFoundInAll = false;
			inputFile = new File(".history/"+w+".stats");
			inputFile.createNewFile();
			tempFile = new File(".history/.tempFile");

			reader = new BufferedReader(new FileReader(inputFile));
			writer = new BufferedWriter(new FileWriter(tempFile));
			while ((currentLine = reader.readLine()) != null) {
				if (currentLine.contains(word)) {
					String[] brokenLine = currentLine.split(" ");
					brokenLine[type.integerValue] = Integer.toString(Integer.parseInt(brokenLine[type.integerValue]) + 1);;
					writer.write(brokenLine[0] + " " + brokenLine[1] + " " + brokenLine[2] + " " + brokenLine[3] + System.getProperty("line.separator"));
					wordFoundInAll = true;
				} else {
					writer.write(currentLine + System.getProperty("line.separator"));
				}
			}
			if (!wordFoundInAll) {
				switch(type) {
				case MASTERED:
					writer.write(word + " 1 0 0" + System.getProperty("line.separator"));
					break;
				case FAULTED:
					writer.write(word + " 0 1 0" + System.getProperty("line.separator"));
					break;
				case FAILED:
					writer.write(word + " 0 0 1" + System.getProperty("line.separator"));
					break;
				}
			}
			tempFile.renameTo(inputFile);
			reader.close();
			writer.close();

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
			layout.show(cards, "Quiz");
			quiz.startQuiz(w);
		} catch (FileNotFoundException e) {
			String message = "The file containing that category has been removed from "+WORDFOLDER;
			JOptionPane.showMessageDialog(new JFrame(), message, "File Not Found", JOptionPane.ERROR_MESSAGE);
			layout.show(cards, menu.toString());
		}
	}
	
	public void returnToMenu() {
		layout.show(cards, "Menu");
	}
	
	private boolean reviewFilesEmpty() {
		for (File f : STATSFOLDER.listFiles())
			if (f.getName().endsWith(".review") && f.length() > 0)
				return false;
		return true;
	}
	
	private boolean wordlistsExist() {
		if (WORDFOLDER.list().length == 0) {
			JOptionPane.showMessageDialog(this, "No wordlist files exist in " + WORDFOLDER.getAbsolutePath() + "\n"
				+ "Please make sure a correctly formatted file exists within " + WORDFOLDER.getName() + " before attempting to start a quiz");
			return false;
		} else {
			return true;
		}
	}

	// TODO: finish
	private boolean isFileWordlist(File f) {
		if (true) {
			
		}
		return false;
	}
}
