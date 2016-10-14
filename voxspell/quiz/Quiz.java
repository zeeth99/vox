package voxspell.quiz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;
import voxspell.BestMediaPlayer;
import voxspell.Card;

@SuppressWarnings("serial")
public class Quiz extends Card implements ActionListener {

	private static final int QUIZ_SIZE = 10;

	private JLabel wordCountLabel;
	protected JLabel categoryLabel;
	private JFormattedTextField inputBox;
	private JLabel feedbackPanel;
	private JButton repeatWord;
	private JButton submitWord;

	protected boolean _firstAttempt;
	protected int _wordNumber;
	private int _wordsCorrect;
	protected List<String> _testingWords;

	protected WordList _wordlist;

	/**
	 * 
	 * @param sp - The SpellingAid that created this
	 */
	public Quiz(SpellingAid sp) {
		super(sp, "New Quiz");

		wordCountLabel = new JLabel();
		wordCountLabel.setBounds(225, 90, 150, 15);
		wordCountLabel.setHorizontalAlignment(JLabel.RIGHT);
		categoryLabel = new JLabel();
		categoryLabel.setBounds(125, 90, 150, 15); // Place this label wherever it fits the best. Kinda awkward where it is at now
		feedbackPanel = new JLabel();
		feedbackPanel.setBounds(125, 230, 300, 15);

		repeatWord = new JButton("Repeat");
		repeatWord.setBounds(135, 175, 85, 25);
		repeatWord.addActionListener(this);
		submitWord = new JButton("Submit");
		submitWord.setBounds(280, 175, 85, 25);
		submitWord.addActionListener(this);
		inputBox = new JFormattedTextField();
		inputBox.setToolTipText("Type here.");
		inputBox.setFont(new Font("Dialog", Font.PLAIN, 16));
		inputBox.setBounds(125, 120, 250, 30);
		inputBox.setColumns(20);
		inputBox.addActionListener(this);
		inputBox.addKeyListener(new KeyAdapter(){ // Only letters and apostrophes can be inputed
			public void keyTyped(KeyEvent e){
				char ch = e.getKeyChar();
				if(!Character.isLetter(ch) && ch != '\''){
					e.consume();
				}
			}
		});

		add(inputBox);
		add(repeatWord);
		add(submitWord);
		add(wordCountLabel);
		add(categoryLabel);
		add(feedbackPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inputBox.grabFocus();
		if (e.getSource() == repeatWord) {
			sayMessage(_testingWords.get(_wordNumber));
		} else if ((e.getSource() == submitWord || e.getSource() == inputBox) && submitWord.isEnabled()) {
			checkWord(inputBox.getText());
		} else if (e.getSource() == menuButton) {
			int option = JOptionPane.showConfirmDialog(this, "If you go to the menu you will lose your progress in your current quiz. \nAre you sure you want to go to the menu?", "Are You Sure?", JOptionPane.OK_CANCEL_OPTION);
			switch(option) {
			case JOptionPane.CANCEL_OPTION:
				break;
			case JOptionPane.OK_OPTION:
				spellingAid.returnToMenu();
				break;
			}
		}
	}

	protected void checkWord(String input) {
		inputBox.setText("");
		String word = _testingWords.get(_wordNumber);
		String festivalMessage;

		boolean correct = false;

		if (input.equalsIgnoreCase(word)) {
			correct = true;
			festivalMessage = "Correct:";
			_wordsCorrect++;
		} else {
			festivalMessage = "Incorrect:";
			if (_firstAttempt) {
				// FIRST FAIL
				_firstAttempt = false;
				sayMessage(festivalMessage+"The word is " + word + ":.:" + word);
				return;
			} 
		}
		spellingAid.updateStats(correct, word, _wordlist);

		_firstAttempt = true;
		_wordNumber++;
		feedbackPanel.setText(_wordsCorrect + " out of " + _wordNumber + " correct so far");

		// If all words have been tested:
		if (_wordNumber == _testingWords.size()) {
			sayMessage(festivalMessage);
			// Check to see if user has completed a level i.e. has gotten 9 out of 10 words correct
			if (_wordsCorrect >= 9) {
				levelCompleteAction();
			} else {
				levelIncompleteAction();
			}
		} else {
			// Test next word
			sayMessage(festivalMessage+"Please spell "+_testingWords.get(_wordNumber));
			wordCountLabel.setText("Word " + (_wordNumber+1) +" of " + _testingWords.size());
		}
	}

	public void startQuiz(WordList w) {
		_wordlist = w;
		categoryLabel.setText(_wordlist.category());
		_wordNumber = 0;
		_wordsCorrect = 0;

		_testingWords = _wordlist.randomWords(QUIZ_SIZE);

		if (_testingWords == null) {
			spellingAid.returnToMenu();
			return;
		}

		_firstAttempt = true;
		wordCountLabel.setText("Word " + (_wordNumber+1) + " of " + _testingWords.size());
		feedbackPanel.setText(_wordsCorrect+" out of " + _wordNumber + " correct so far");
		sayMessage("Please spell " + _testingWords.get(_wordNumber));
		inputBox.grabFocus();
	}

	private void sayMessage(String message) {
		enableButtons(false);
		(new Festival(message, this)).execute();
	}

	public void enableButtons(boolean enable) {
		Color c = Color.GRAY;
		if (enable) { 
			c = Color.WHITE;
		}
		repeatWord.setBackground(c);
		submitWord.setBackground(c);
		repeatWord.setEnabled(enable);
		submitWord.setEnabled(enable);
	}

	/* Decides on what to do when the level is completed depending on what the user
	 * chooses to do and what level they are on [FOR NORMAL QUIZ ONLY]
	 */
	protected void levelCompleteAction() {
		_wordNumber = 0;
		_wordsCorrect = 0;
		// Give option for video reward before asking to progress to next level
		String[] options = new String[] {"Repeat Level", "Play Video", "Return to Main Menu"};
		int option = JOptionPane.showOptionDialog(this, "You have completed this level!\nWhat would you like to do?", "Congratulations!",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			startQuiz(_wordlist);
		} else if (option == 1) {
			selectFilterAndPlay();
			spellingAid.returnToMenu();
		} else {
			spellingAid.returnToMenu();
		}
	}

	/* Decides on what to do when the level is not completed [FOR NORMAL QUIZ ONLY]
	 */
	protected void levelIncompleteAction() {
		String[] options = new String[] {"Repeat level","Return to Main Menu"};
		int option = JOptionPane.showOptionDialog(this, "You didn't complete the level.\nTo complete a level, you must get 9 out of the 10 words correct. What would you like to do?",
				"Unfortunate my friend", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			startQuiz(_wordlist);
		} else {
			spellingAid.returnToMenu();
		}
	}

	private void selectFilterAndPlay() {
		if (!videoExists()) {
			JOptionPane.showMessageDialog(this, BestMediaPlayer.NORMAL_VIDEO+" does not exist within "+ClassLoader.getSystemClassLoader().getResource(".").getPath()+"\n"
					+ "If you ran VOXSPELL without using the runVoxspell.sh script, then "+BestMediaPlayer.NORMAL_VIDEO+" doesn't exist within your home directory\n"
					+ "Please make sure this file exists in the correct directory as to enable the video to be played");
			return;
		}
		String[] options = new String[] {"Normal","Negative"};
		int option = JOptionPane.showOptionDialog(this, "Select a filter", "#AllNatural#NoFilter#IWokeUpLikeThis",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			new BestMediaPlayer(BestMediaPlayer.Filter.NORMAL);
		} else {
			new BestMediaPlayer(BestMediaPlayer.Filter.NEGATIVE);
		}
	}

	private boolean videoExists() {
		File f = new File(BestMediaPlayer.NORMAL_VIDEO);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	public String cardName() {
		return "Quiz";
	}
}