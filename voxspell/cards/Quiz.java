package voxspell.cards;

import java.awt.Color;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;
import voxspell.BestMediaPlayer;
import voxspell.Festival;

@SuppressWarnings("serial")
public class Quiz extends Card implements ActionListener {
	
	private static final int QUIZ_SIZE = 10;

	private JLabel quizInstrLabel;
	private JLabel levelLabel;
	private JFormattedTextField quizInputBox;
	private JLabel quizFeedbackLabel;
	private JButton repeatWord;
	private JButton submitWord;

	private boolean _firstAttempt;
	private boolean _reviewMode;
	private boolean _reviewSpellOut;
	private int _wordNumber;
	private int _wordsCorrect;
	private List<String> _testingWords;
	
	private int _level;
	
	private BestMediaPlayer _player;
	
	// Object used for text to speech. Could be an instance variable or local
	private Festival _festival;
	
	// TODO: Have a button which leads to statistics for that given session (all levels) which can be pressed during the quiz
	// TODO: Have feedback during the quiz (a label) which tells the user how many words they have gotten correct so far, on a given level.
	public Quiz(SpellingAid sp) {
		super(sp, "Quiz");
		
		spellingAid = sp;
		_wordNumber = 0;
		_wordsCorrect = 0;

		quizInstrLabel = new JLabel();
		quizInstrLabel.setBounds(125, 90, 250, 15);
		levelLabel = new JLabel();
		levelLabel.setBounds(125, 70, 150, 15); // Place this label wherever it fits the best. Kinda awkward where it is at now
		quizFeedbackLabel = new JLabel();
		quizFeedbackLabel.setBounds(135, 220, 300, 15);
		
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

		add(quizInputBox);
		add(repeatWord);
		add(submitWord);
		add(quizInstrLabel);
		add(levelLabel);
		add(quizFeedbackLabel);

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

	private void checkWord() {
		String input = quizInputBox.getText();
		String word = _testingWords.get(_wordNumber);
		quizInputBox.setText("");

		String festivalMessage;
		
		if (_reviewSpellOut) { // Check if word is spelt correctly on their last chance
			_reviewSpellOut = false;
			if (input.equalsIgnoreCase(word)) {
				// TODO: Remove word from failed list. Not sure whether word is mastered or faulted
				// ((SpellingAid) spellingAid).updateStats("faulted", word);
				festivalMessage = "correct";
				_wordsCorrect++;
				removeFromReview(word);
			} else {
				festivalMessage = "incorrect";
				addWordToReview(word, _level);
			}
		} else if (_firstAttempt) {
			if (input.equalsIgnoreCase(word)) {
				((SpellingAid) spellingAid).updateStats("mastered", word);
				festivalMessage = "correct";
				_wordsCorrect++;
				removeFromReview(word);
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
				removeFromReview(word);
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
				addWordToReview(word, _level);
			}
		}
		_firstAttempt = true;
		
		// Check to see if user has completed a level i.e. has gotten 9 out of 10 words correct
		if (_wordsCorrect >= 9 && !_reviewMode) {
			quizFeedbackLabel.setText("CONGRATULATIONS. You completed the level");
			sayMessage(festivalMessage);
			levelCompleteAction();
			return;
		}
		if (_wordNumber + 1 == _testingWords.size()) {
			_wordNumber = 0;
			_wordsCorrect = 0;
			sayMessage(festivalMessage);
			if (_reviewMode) {
				reviewLevelCompleteAction();
			} else {
				String[] options = new String[] {"Repeat level","Return to Main Menu"};
				int option = JOptionPane.showOptionDialog(this, "You didn't complete the level.\nTo complete a level, you must get 9 out of the 10 words correct. What would you like to do?",
						"Unfortunate my friend", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == JOptionPane.YES_OPTION) {
					spellingAid.startQuiz(_level);
				} else {
					spellingAid.returnToMenu();
				}
			}
		} else {
			_wordNumber++;
			quizFeedbackLabel.setText(_wordsCorrect+" out of " + _wordNumber + " correct so far");
			sayMessage(festivalMessage+".. Please spell "+_testingWords.get(_wordNumber));
			quizInstrLabel.setText("Please spell word " + (_wordNumber+1) +" of " + _testingWords.size());
		}
	}

	public void startQuiz(int level) {
		_level = level;
		levelLabel.setText("Level "+_level);
		
		if (_reviewMode) {
			heading.setText("Review Quiz");
			File f = new File(".history/review/level"+level);
			_testingWords = randomWords(f, level);
			if (noWordsToReview()) {
				return;
			}
		} else {
			heading.setText("New Quiz");
			_testingWords = randomWords(SpellingAid.WORDLIST, level);
		}
		_firstAttempt = true;
		_reviewSpellOut = false;
		quizInstrLabel.setText("Please spell word " + (_wordNumber+1) + " of " + _testingWords.size());
		quizFeedbackLabel.setText(_wordsCorrect+" out of " + _wordNumber + " correct so far");
		sayMessage("Please spell " + _testingWords.get(_wordNumber));
		quizInputBox.grabFocus();
	}

	public void setReviewMode(boolean isTrue) {
		_reviewMode = isTrue;
	}
	
	private void sayMessage(String message) {
		disableButtons();
		_festival = new Festival(message, this);
		_festival.execute();
	}
	
	private void disableButtons() {
		repeatWord.setBackground(Color.GRAY);
		submitWord.setBackground(Color.GRAY);
		repeatWord.setEnabled(false);
		submitWord.setEnabled(false);
	}
	
	public void enableButtons() {
		repeatWord.setBackground(Color.WHITE);
		submitWord.setBackground(Color.WHITE);
		repeatWord.setEnabled(true);
		submitWord.setEnabled(true);
	}
	
	/* Decides on what to do when the level is completed depending on what the user
	 * chooses to do and what level they are on [FOR NORMAL QUIZ ONLY]
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
				_player = new BestMediaPlayer();
				spellingAid.returnToMenu();
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
				_player = new BestMediaPlayer();
				spellingAid.returnToMenu();
			} else {
				spellingAid.returnToMenu();
			}
		}
	}
	
	private List<String> randomWords(File f, int level) {
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
		Random rnd = new Random();
		for (int i = 0; i < QUIZ_SIZE; i++) {
			String word = tempList.get(rnd.nextInt(tempList.size()));
			tempList.remove(word);
			wordList.add(word);
			if (wordList.isEmpty()) {
				break;
			}
		}
		return wordList;
	}
	
	private void addWordToReview(String word, int level) {
		try {	
			String currentLine;
			File inputFile = new File(".history/review/level"+level);
			File tempFile = new File(".history/.tempFile");
	
			BufferedReader reader = new BufferedReader(new FileReader(inputFile));
			BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
	
			while((currentLine = reader.readLine()) != null) {
			    String trimmedLine = currentLine.trim();
			    if (trimmedLine.equals(word)) continue;
			    writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.write(word + System.getProperty("line.separator"));
			writer.close();
			reader.close();
			tempFile.renameTo(inputFile);
		} catch (Exception e) { }
	}
	
	private boolean noWordsToReview() {
		if (_testingWords.size() == 0) {
			quizInstrLabel.setText("");
			if (_level != 11) {
				String[] options = new String[] {"Continue", "Return to Main Menu"};
				int option = JOptionPane.showOptionDialog(this, "There are no words to review on this level",
						"Select an option", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					spellingAid.startQuiz(_level+1);
				} else {
					spellingAid.returnToMenu();
				}
			} else {
				JOptionPane.showMessageDialog(this, "There are no words to review on this level");
				JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
				spellingAid.returnToMenu();
			}
			return true;
		}
		return false;
	}
	
	private void reviewLevelCompleteAction() {
		File f = new File(".history/review/level"+_level);
		if (f.length() > 0) {
			String[] options = new String[] {"Repeat level","Return to Main Menu"};
			int option = JOptionPane.showOptionDialog(this, "There are still some words left to review on this level\nTo progress to further levels"
					+ ", all words on this level must be cleared", "But wait.. there's more", 
					JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
			if (option == 0) {
				spellingAid.startQuiz(_level);
			} else {
				spellingAid.returnToMenu();
			}
		} else {
			quizFeedbackLabel.setText("CONGRATULATIONS. You completed this level");
			if (_level != 11) {
				String[] options = new String[] {"Continue","Return to Main Menu"};
				int option = JOptionPane.showOptionDialog(this, "You have cleared all words on this level", "Congratulations!", 
						JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
				if (option == 0) {
					spellingAid.startQuiz(_level + 1);
				} else {
					spellingAid.returnToMenu();
				}
			} else {
				JOptionPane.showMessageDialog(this, "You have completed review mode", "Congratulations!", JOptionPane.PLAIN_MESSAGE);
				spellingAid.returnToMenu();
			}
		}
	}
	
	protected void removeFromReview(String wordToBeRemoved) {
		File review = new File(".history/review/level"+_level);
		File temp = new File(".history/.tempFile");
		try {
			/* Following code retrieved and slightly modified from 
			 * http://stackoverflow.com/questions/1377279/find-a-line-in-a-file-and-remove-it */
			BufferedReader reader = new BufferedReader(new FileReader(review));
			BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
		
			String currentLine;

			while((currentLine = reader.readLine()) != null) {
		    
				String trimmedLine = currentLine.trim();
				if(trimmedLine.equals(wordToBeRemoved)) {
					continue;
				}
				writer.write(currentLine + System.getProperty("line.separator"));
			}
			writer.close(); 
			reader.close(); 
			temp.renameTo(review);
			
		} catch (Exception e) { 
			JOptionPane.showMessageDialog(this, "An error has occured due to critical files missing from "
					+ ClassLoader.getSystemClassLoader().getResource(".").getPath() +"\nProgram will now exit. Opening program again will fix this");
			System.exit(2);
		}
	}
	
}