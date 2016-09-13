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

	private CardLayout layout = new CardLayout();
	private JPanel cards = new JPanel();

	// Cards
	private JPanel menu = new JPanel();
	private JPanel levelSelect = new JPanel();
	private JPanel quiz = new JPanel();
	private JPanel stats = new JPanel();

	private JLabel menuLabel;
	private JLabel levelSelectLabel;
	private JLabel quizLabel;
	private JLabel quizInstrLabel;
	private JLabel statsLabel;

	private JFormattedTextField quizInputBox;

	private JScrollPane scrollPane;

	private JTable statsTable;

	private JButton newSpellingQuiz;
	private JButton reviewMistakes;
	private JButton viewStatistics;
	private JButton clearStatistics;
	private JButton repeatWord;
	private JButton submitWord;
	private JButton backToMenuQuiz;
	private JButton backToMenuStats;

	private boolean _firstAttempt;
	private boolean _reviewMode;
	private int _wordNumber;
	private List<String> _testingWords;


	private SpellingAid(String[] args) throws FileNotFoundException {
		setResizable(false);
		setTitle("Spelling Aid");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		_wordNumber = 0;


		cards.setLayout(layout);

		// Set up Menu screen
		{
			menuLabel = new JLabel("Welcome to the Spelling Aid");
			menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
			menuLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
			menuLabel.setBounds(0, 0, 500, 60);
			newSpellingQuiz = new JButton("New Spelling Quiz");
			newSpellingQuiz.setFont(new Font("Dialog", Font.BOLD, 16));
			newSpellingQuiz.setBounds(100, 90, 300, 50);
			newSpellingQuiz.addActionListener(this);
			reviewMistakes = new JButton("Review Mistakes");
			reviewMistakes.setFont(new Font("Dialog", Font.BOLD, 16));
			reviewMistakes.setBounds(100, 150, 300, 50);
			reviewMistakes.addActionListener(this);
			viewStatistics = new JButton("View Statistics");
			viewStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
			viewStatistics.setBounds(100, 210, 300, 50);
			viewStatistics.addActionListener(this);
			clearStatistics = new JButton("Clear Statistics");
			clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
			clearStatistics.setBounds(100, 270, 300, 50);
			clearStatistics.addActionListener(this);
			cards.add(menu, "Menu");

			menu.setLayout(null);
			menu.add(menuLabel);
			menu.add(reviewMistakes);
			menu.add(viewStatistics);
			menu.add(clearStatistics);
			menu.add(newSpellingQuiz);
		}

		// Set up Level Select screen
		{
			levelSelectLabel = new JLabel("Select Your Quiz Level");
			levelSelectLabel.setHorizontalAlignment(SwingConstants.CENTER);
			levelSelectLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
			levelSelectLabel.setBounds(0, 0, 500, 60);
			cards.add(levelSelect, "Level Select");

			levelSelect.add(levelSelectLabel);
		}

		// Set up Quiz screen
		{
			quizLabel = new JLabel();
			quizLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
			quizLabel.setHorizontalAlignment(SwingConstants.CENTER);
			quizLabel.setText("Quiz");
			quizLabel.setBounds(0, 0, 500, 60);
			quizLabel.setPreferredSize(new Dimension(300, 100));
			quizInstrLabel = new JLabel("Please type the word you hear:");
			quizInstrLabel.setBounds(125, 90, 250, 15);

			repeatWord = new JButton("Repeat");
			repeatWord.setBounds(135, 175, 85, 25);
			repeatWord.addActionListener(this);
			submitWord = new JButton("Submit");
			submitWord.setBounds(280, 175, 85, 25);
			submitWord.addActionListener(this);
			quizInputBox = new JFormattedTextField();
			quizInputBox.setToolTipText("Type here.");
			quizInputBox.setFont(new Font("Dialog", Font.PLAIN, 16));
			quizInputBox.setBounds(125, 120, 250, 30);
			quizInputBox.setColumns(20);
			quizInputBox.addKeyListener(new KeyAdapter(){ // Only letters can be inputed
	            public void keyTyped(KeyEvent e){
	            	String text = quizInputBox.getText();
	                char ch = e.getKeyChar();
	                if(!Character.isLetter(ch)){
	                    quizInputBox.setText(text);
	                    e.consume();
	                }
	            }
	        });

			quiz.setLayout(null);
			quiz.add(quizInputBox);
			quiz.add(repeatWord);
			quiz.add(submitWord);
			quiz.add(quizLabel);
			quiz.add(quizInstrLabel);

			cards.add(quiz, "Quiz");
		}

		// Set up Stats screen
		{
			statsLabel = new JLabel("Spelling Statistics");
			statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
			statsLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
			statsLabel.setBounds(0, 0, 500, 60);
			backToMenuStats = new JButton("Menu");
			backToMenuStats.setBounds(12, 18, 73, 25);
			backToMenuStats.addActionListener(this);
			statsTable = new JTable();
			statsTable.setShowGrid(true);
			stats.setLayout(null);

			stats.add(backToMenuStats);
			stats.add(statsLabel);

			int lineCount = 0;
			Scanner scanCount = new Scanner(new File(".history/all"));
			while (scanCount.hasNextLine()) {
				lineCount++;
				scanCount.nextLine();
			}
			scanCount.close();

			String[] tableHeadings = {"Word", "Times Mastered", "Times Faulted", "Time Failed"};
			String[][] tableContents = new String[lineCount][4];
			Scanner sc = new Scanner(new File(".history/all"));
			for (int i = 0; i < lineCount; i++) {
				for (int j = 0; j < 4; j++) {
					tableContents[i][j] = sc.next();
				}
			}
			sc.close();

			statsTable = new JTable(tableContents, tableHeadings);
			statsTable.setEnabled(false);
			statsTable.setAutoCreateRowSorter(true);

			scrollPane = new JScrollPane(statsTable);
			scrollPane.setBounds(0, 60, 500, 320);
			stats.add(scrollPane);

			cards.add(stats, "Stats");
		}

		getContentPane().add(cards);
		layout.show(cards, "Menu");

		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == newSpellingQuiz) {
			newQuiz();
		} else if (e.getSource() == reviewMistakes) {
			review();
		} else if (e.getSource() == viewStatistics) {
			layout.show(cards, "Stats");
			statsTable.repaint();
		} else if (e.getSource() == clearStatistics) {
			clearStats();
		} else if (e.getSource() == backToMenuQuiz || e.getSource() == backToMenuStats) {
			layout.show(cards, "Menu");
		} else if (e.getSource() == repeatWord) {
			festival(_testingWords.get(_wordNumber));
		} else if (e.getSource() == submitWord) {
			checkWord();
		}

	}

	private void festival(String message) {
		try {

			ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo \"" + message + "\" | festival --tts");

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
				System.out.println(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<String> randomWords(File f, int level) {
		List<String> tempList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
			while (sc.hasNextLine()) {
				if (sc.nextLine().equals("%Level "+level)) {
						break;
				}
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (line.charAt(0) == '%') {
					break;
				}
				tempList.add(line);
			}
			sc.close();
			if (tempList.size() > 10) {
				Random rnd = new Random();
				for (int i = 0; i < 10; i++) {
					String word = tempList.get(rnd.nextInt(tempList.size()));
					tempList.remove(word);
					wordList.add(word);
				}
			} else {
				return tempList;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return wordList;
	}

	private void updateStats(String type, String word) {
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

			updateStatsTable(type, word);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void checkWord() {
		String input = quizInputBox.getText();
		String word = _testingWords.get(_wordNumber);
		quizInputBox.setText("");
		quizInputBox.grabFocus();


		if (_firstAttempt) {
			if (input.equals(word)) {
				updateStats("mastered", word);
				festival("correct");
			} else {
				_firstAttempt = false;
				festival("incorrect");
				festival("Please spell" + _testingWords.get(_wordNumber));
				return;
			}
		} else {
			if (input.equals(word)) {
				updateStats("faulted", word);
				festival("correct");
			} else {
				updateStats("failed", word);
				festival("incorrect");
				if (_reviewMode) {
					String spellOutWord = "";
					for (int i = 0; i < word.length(); i++) {
						spellOutWord += word.charAt(i) + ". ";
					}
					festival(spellOutWord);
				}
			}
		}
		_firstAttempt = true;
		if (_wordNumber + 1 == _testingWords.size()) {
			layout.show(cards, "Menu");
			_wordNumber = 0;
		} else {
			_wordNumber++;
			festival("Please spell " + _testingWords.get(_wordNumber));
		}

	}

	private void updateStatsTable(String type, String word) {
		for (int i = 0; i < statsTable.getRowCount(); i++) {
			if (statsTable.getValueAt(i, 0).equals(word)) {
				int column, aValue;
				if (type.equals("mastered")) {
					column = 1;
				} else if (type.equals("faulted")) {
					column = 2;
				} else {
					column = 3;
				}
				aValue = Integer.parseInt((String)statsTable.getValueAt(i, column));
				statsTable.setValueAt(""+aValue, i, column);
			}
		}
	}

	private void SpellingQuiz(File f, int level) {
		_firstAttempt = true;
		_testingWords = randomWords(f, level);
		layout.show(cards, "Quiz");
		festival("Please spell " + _testingWords.get(_wordNumber));
		quizInputBox.grabFocus();
	}

	protected void newQuiz() {
		quizLabel.setText("New Quiz");
		SpellingQuiz(new File("wordlist"));
		_reviewMode = false;
	}

	protected void review() {
		if ((new File(".history/failed")).length() != 0) {
			quizLabel.setText("Review Quiz");
			SpellingQuiz(new File(".history/failed"));
			_reviewMode = true;
		} else {
			JOptionPane.showMessageDialog(this, "There are no words to revise.\nWell done!", "Nothing To Revise", JOptionPane.PLAIN_MESSAGE);
		}
	}

	protected void clearStats() {
		JFrame popupFrame = new JFrame();
		String message = "This will permanently delete all of your spelling history.\n"
				+ "Are you sure you want to do this?";
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			String[] historyFileList = {"mastered", "faulted", "failed", "all"};
			for (int i = 0; i < 4; i++) (new File(".history/" + historyFileList[i])).delete();
			createStatsFiles();
			statsTable.selectAll();
			statsTable.clearSelection();
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

}
