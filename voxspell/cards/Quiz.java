package voxspell.cards;

import java.awt.Color;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;
import voxspell.Festival;

@SuppressWarnings("serial")
public class Quiz extends JPanel implements ActionListener {
	
	private static final int MAXWORDS = 10;
	
	private SpellingAid spellingAid;

	private JLabel quizLabel;
	private JLabel quizInstrLabel;
	private JLabel levelLabel;
	private JFormattedTextField quizInputBox;
	public static JButton repeatWord;
	public static JButton submitWord;

	private boolean _firstAttempt;
	private boolean _reviewMode;
	private boolean _reviewSpellOut;
	private int _wordNumber;
	private int _wordsCorrect;
	private List<String> _testingWords;
	
	private int _level;
	
	// Object used for text to speech. Could be an instance variable or local
	private Festival _festival;
	
	// TODO: Have a button which leads to statistics for that given session (all levels) which can be pressed during the quiz
	// TODO: Have feedback during the quiz (a label) which tells the user how many words they have gotten correct so far, on a given level.
	public Quiz(SpellingAid sp) {
		spellingAid = sp;
		_wordNumber = 0;
		_wordsCorrect = 0;

		quizLabel = new JLabel();
		quizLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		quizLabel.setHorizontalAlignment(SwingConstants.CENTER);
		quizLabel.setText("Quiz");
		quizLabel.setBounds(0, 0, 500, 60);
		quizLabel.setPreferredSize(new Dimension(300, 100));
		quizInstrLabel = new JLabel();
		quizInstrLabel.setBounds(125, 90, 250, 15);
		levelLabel = new JLabel();
		levelLabel.setBounds(125, 70, 150, 15); // Place this label wherever it fits the best. Kinda awkward where it is at now

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
		
		// TODO: Apostrophes are present in some of the words and this only alloweds letters so needs to change
		// to allow apostrophes
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
		add(levelLabel);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		quizInputBox.grabFocus();
		if (e.getSource() == repeatWord) {
			sayMessage(_testingWords.get(_wordNumber));
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
		
		if (_reviewSpellOut) { // Check if word is spelt correctly on thier last chance
			_reviewSpellOut = false;
			if (input.equalsIgnoreCase(word)) {
				// TODO: Remove word from failed list. Not sure whether word is mastered or faulted
				// ((SpellingAid) spellingAid).updateStats("faulted", word);
				festivalMessage = "correct";
			} else {
				festivalMessage = "incorrect";
			}
		} else if (_firstAttempt) {
			if (input.equalsIgnoreCase(word)) {
				((SpellingAid) spellingAid).updateStats("mastered", word);
				festivalMessage = "correct";
				_wordsCorrect++;
			} else {
				_firstAttempt = false;
				sayMessage("Incorrect. The word is " + _testingWords.get(_wordNumber) + ".. " + _testingWords.get(_wordNumber));
				return;
			}
		} else {
			if (input.equalsIgnoreCase(word)) {
				((SpellingAid) spellingAid).updateStats("faulted", word);
				festivalMessage = "correct";
				_wordsCorrect++;
			} else {
				((SpellingAid) spellingAid).updateStats("failed", word);
				festivalMessage = "incorrect.. ";
				if (_reviewMode && !_reviewSpellOut) {
					_reviewSpellOut = true;
					festivalMessage += "the word is spelt.. ";
					// Spell out word if reviewing
					String spellOutWord = "";
					for (int i = 0; i < word.length(); i++) {
						spellOutWord += word.charAt(i) + ".. ";
					}
					festivalMessage += spellOutWord;
					sayMessage(festivalMessage);
					return;
				}
			}
		}
		_firstAttempt = true;
		
		// Check to see if user has completed a level i.e. has gotten 9 out of 10 words correct
		if (_wordsCorrect >= 9) {
			sayMessage(festivalMessage);
			levelCompleteAction();
			return;
		}
		if (_wordNumber + 1 == _testingWords.size()) {
			_wordNumber = 0;
			_wordsCorrect = 0;
			sayMessage(festivalMessage);
			String[] options = new String[] {"Repeat level","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "You have failed to complete the level.\nWould you like to do?",
					"Unfortunate my friend", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == JOptionPane.YES_OPTION) {
				spellingAid.startQuiz(_level);
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			_wordNumber++;
			sayMessage(festivalMessage+".. Please spell "+_testingWords.get(_wordNumber));
			quizInstrLabel.setText("Please spell word " + (_wordNumber+1) +" of " + MAXWORDS);
		}
	}

	public void startQuiz(int level) {
		_level = level;
		levelLabel.setText("Level "+_level);
		
		if (_reviewMode) {
			quizLabel.setText("Review Quiz");
			_testingWords = randomWords(SpellingAid.REVIEWLIST, level);
		} else {
			quizLabel.setText("New Quiz");
			_testingWords = randomWords(SpellingAid.WORDLIST, level);
		}
		_firstAttempt = true;
		_reviewSpellOut = false;
		quizInstrLabel.setText("Please spell word " + (_wordNumber+1) + " of " + MAXWORDS);
		sayMessage("Please spell " + _testingWords.get(_wordNumber));
		quizInputBox.grabFocus();
	}

	public void setReviewMode(boolean isTrue) {
		_reviewMode = isTrue;
	}
	
	private void sayMessage(String message) {
		disableButtons();
		_festival = new Festival(message);
		_festival.execute();
	}
	
	private void disableButtons() {
		repeatWord.setBackground(Color.GRAY);
		submitWord.setBackground(Color.GRAY);
		repeatWord.setEnabled(false);
		submitWord.setEnabled(false);
	}
	
	public static void enableButtons() {
		repeatWord.setBackground(Color.WHITE);
		submitWord.setBackground(Color.WHITE);
		repeatWord.setEnabled(true);
		submitWord.setEnabled(true);
	}
	
	/* Decides on what to do when the level is completed depending on what the user
	 * chooses to do and what level they are on
	 */
	public void levelCompleteAction() {
		_wordNumber = 0;
		_wordsCorrect = 0;
		if (_level != 11) {
			// TODO: Give option for video reward before asking to progress to next level
			// TODO: Give option to return to main menu after a level is completed as well as to an option to repeat a level
			String[] options = new String[] {"Next Level","Repeat Level","Play Video", "Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "You have completed this level!\nWhat would you like to do?", "Congratulations!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_level + 1);
			} else if (option == 1) {
				spellingAid.startQuiz(_level);
			} else if (option == 2) {
				// TODO: Play video
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			// TODO: Give option for video reward
			String[] options = new String[] {"Repeat Level","Play Video","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "You have completed this level!\nWhat would you like to do?", "Congratulations!",
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_level);
			} else if (option == 1) {
				// TODO: Play video
			} else {
				spellingAid.returnToMenu();
			}
		}
	}
}
