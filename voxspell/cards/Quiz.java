package voxspell.cards;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == repeatWord) {
			SpellingAid.festival(_testingWords.get(_wordNumber));
		} else if (e.getSource() == submitWord) {
			checkWord();
		}
	}

	private List<String> randomWords(File f, int level) {
		// Commented out code is to be added when level selection is implemented
		List<String> tempList = new ArrayList<String>();
		List<String> wordList = new ArrayList<String>();
		try {
			Scanner sc = new Scanner(f);
			// while (sc.hasNextLine()) {
			// 	if (sc.nextLine().equals("%Level "+level)) {
			// 			break;
			// 	}
			// }
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				// if (line.charAt(0) == '%') {
				// 	break;
				// }
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

	private void checkWord() {
		String input = quizInputBox.getText();
		String word = _testingWords.get(_wordNumber);
		quizInputBox.setText("");
		quizInputBox.grabFocus();

		String festivalMessage;
		if (_firstAttempt) {
			if (input.equals(word)) {
				((SpellingAid) spellingAid).updateStats("mastered", word);
				festivalMessage = "correct";
			} else {
				_firstAttempt = false;
				SpellingAid.festival("Incorrect. Please spell" + _testingWords.get(_wordNumber));
				return;
			}
		} else {
			if (input.equals(word)) {
				((SpellingAid) spellingAid).updateStats("faulted", word);
				festivalMessage = "correct";
			} else {
				((SpellingAid) spellingAid).updateStats("failed", word);
				festivalMessage = "incorrect.";
				if (_reviewMode) {
					// Spell out word if reviewing
					String spellOutWord = "";
					for (int i = 0; i < word.length(); i++) {
						spellOutWord += word.charAt(i) + ". ";
					}
					festivalMessage += spellOutWord;
				}
			}
		}
		SpellingAid.festival(festivalMessage);
		_firstAttempt = true;
		if (_wordNumber + 1 == _testingWords.size()) {
			spellingAid.returnToMenu();
			_wordNumber = 0;
		} else {
			_wordNumber++;
			SpellingAid.festival("Please spell " + _testingWords.get(_wordNumber));
		}

	}

	public void startQuiz(int level) {
		File f;
		if (_reviewMode) {
			quizLabel.setText("Review Quiz");
			f = SpellingAid.REVIEWLIST;
		} else {
			quizLabel.setText("New Quiz");
			f = SpellingAid.WORDLIST;
		}
		_firstAttempt = true;
		_testingWords = randomWords(f, level);
		SpellingAid.festival("Please spell " + _testingWords.get(_wordNumber));
		quizInputBox.grabFocus();
	}

	public void setReviewMode(boolean isTrue) {
		_reviewMode = isTrue;
	}

}
