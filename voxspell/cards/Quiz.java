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
import voxspell.Festival;

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
	
	// Object used for text to speech. Could be an instance variable or local
	private Festival _festival;

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
		quizInputBox.grabFocus();
		if (e.getSource() == repeatWord) {
			_festival = new Festival(_testingWords.get(_wordNumber));
			_festival.execute();
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
			if (!_reviewMode) {
				while (sc.hasNextLine()) {
					if (sc.nextLine().equals("%Level "+level)) {
						break;
					}
				}
			}
			while (sc.hasNextLine()) {
				String line = sc.nextLine();
				if (!_reviewMode) {
					 if (line.charAt(0) == '%') {
					 	break;
					 }
				}
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

		String festivalMessage;
		
		if (_firstAttempt) {
			if (input.equalsIgnoreCase(word)) {
				((SpellingAid) spellingAid).updateStats("mastered", word);
				festivalMessage = "correct";
			} else {
				_firstAttempt = false;
				_festival = new Festival("Incorrect. The word is" + _testingWords.get(_wordNumber) + ".. " + _testingWords.get(_wordNumber));
				_festival.execute();
				return;
			}
		} else {
			if (input.equalsIgnoreCase(word)) {
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
		_festival = new Festival(festivalMessage);
		_festival.execute();
		_firstAttempt = true;
		if (_wordNumber + 1 == _testingWords.size()) {
			_wordNumber = 0;
			spellingAid.returnToMenu();
		} else {
			_wordNumber++;
			//TODO: fix
			//SpellingAid.festival("Please spell " + _testingWords.get(_wordNumber));
		}

	}

	public void startQuiz(int level) {
		if (_reviewMode) {
			quizLabel.setText("Review Quiz");
			_testingWords = randomWords(SpellingAid.REVIEWLIST, level);
		} else {
			quizLabel.setText("New Quiz");
			_testingWords = randomWords(SpellingAid.WORDLIST, level);
		}
		_firstAttempt = true;
		_festival = new Festival("Please spell " + _testingWords.get(_wordNumber));
		_festival.execute();
		quizInputBox.grabFocus();
	}

	public void setReviewMode(boolean isTrue) {
		_reviewMode = isTrue;
	}

}
