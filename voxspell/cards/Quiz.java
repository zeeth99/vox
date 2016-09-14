package voxspell.cards;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Quiz extends JPanel implements ActionListener {
	private SpellingAid spellingAid;

	private JLabel quizLabel;
	private JLabel quizInstrLabel;
	private JFormattedTextField quizInputBox;
	private JButton repeatWord;
	private JButton submitWord;

	private boolean _firstAttempt;
	private boolean _reviewMode;
	private int _wordNumber;
	private List<String> _testingWords;

	public Quiz(SpellingAid sp) {
		spellingAid = sp;
		_wordNumber = 0;

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

		setLayout(null);
		add(quizInputBox);
		add(repeatWord);
		add(submitWord);
		add(quizLabel);
		add(quizInstrLabel);
	}

	private static void festival(String message) {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo \"" + message + "\" | festival --tts");
		try {

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == repeatWord) {
			festival(_testingWords.get(_wordNumber));
		} else if (e.getSource() == submitWord) {
			checkWord();
		}
	}

	public List<String> randomWords(File f, int level) {
		// Commented out code is to be added when level selection is implemented
		List<String> tempList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
//			while (sc.hasNextLine()) {
//				if (sc.nextLine().equals("%Level "+level)) {
//						break;
//				}
//			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
//				if (line.charAt(0) == '%') {
//					break;
//				}
				tempList.add(line);
			}
			sc.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
					// Spell out word if reviewing
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
			spellingAid.returnToMenu();
			_wordNumber = 0;
		} else {
			_wordNumber++;
			festival("Please spell " + _testingWords.get(_wordNumber));
		}

	}

	public void startQuiz(File f, int level) {
		_firstAttempt = true;
		_testingWords = randomWords(f, level);
		festival("Please spell " + _testingWords.get(_wordNumber));
		quizInputBox.grabFocus();
	}

	public void newQuiz() {
		quizLabel.setText("New Quiz");
		startQuiz(SpellingAid.WORDLIST, 0); //TODO: level select
		_reviewMode = false;
	}

	public void review() {
		if (SpellingAid.REVIEWLIST.length() > 0) {
			quizLabel.setText("Review Quiz");
			startQuiz(SpellingAid.REVIEWLIST, 0); //TODO: level select
			_reviewMode = true;
		}
	}

}
