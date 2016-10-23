package voxspell.quiz;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;
import voxspell.BestMediaPlayer;
import voxspell.BestMediaPlayer.Video;
import voxspell.Card;
import voxspell.FileAccess;

/**
 * Screen to test the user on a list of up to {@value #QUIZ_SIZE} words.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Quiz extends Card implements ActionListener {

	private static final int QUIZ_SIZE = 10;
	private static final ImageIcon CORRECT = new ImageIcon("media/Correct.png");
	private static final ImageIcon INCORRECT = new ImageIcon("media/Incorrect.png");

	private JLabel wordCountLabel;
	private JFormattedTextField inputBox;
	private JLabel correctIncorrect;
	private JLabel feedbackPanel;
	private JButton repeatWord;
	private JButton submitWord;

	protected boolean _firstAttempt;
	protected int _wordNumber;
	private int _wordsCorrect;
	protected List<String> _testingWords;

	protected WordList _wordlist;

	/**
	 * Set up the GUI
	 * @param sp - The SpellingAid that created this
	 */
	public Quiz(SpellingAid sp) {
		super(sp, "New Quiz");

		wordCountLabel = new JLabel("Word 0 out of 0");
		wordCountLabel.setBounds(125, 90, 150, 15);
		feedbackPanel = new JLabel("0 out of 0 correct");
		feedbackPanel.setBounds(125, 230, 300, 15);

		// Button used to hear the word to spell again
		repeatWord = new JButton("Repeat");
		repeatWord.setBounds(135, 175, 85, 25);
		repeatWord.addActionListener(this);
		
		// Button to submit a proposed spelling of the word
		submitWord = new JButton("Submit");
		submitWord.setBounds(280, 175, 85, 25);
		submitWord.addActionListener(this);
		
		// Box to type the word to spell
		inputBox = new JFormattedTextField();
		inputBox.setToolTipText("Type here.");
		inputBox.setFont(new Font("Dialog", Font.PLAIN, 16));
		inputBox.setBounds(125, 120, 250, 30);
		inputBox.addActionListener(this);
		inputBox.addKeyListener(new KeyAdapter(){ // Only letters and apostrophes can be inputed
			public void keyTyped(KeyEvent e){
				char ch = e.getKeyChar();
				if(!(Character.isLetter(ch) || ch=='\'' || ch==' ' || ch=='-'))
					e.consume();
			}
		});
		
		// Shows a tick or a cross after attempting a word
		correctIncorrect = new JLabel("");
		correctIncorrect.setBounds(375, 111, 48, 48);
		
		add(correctIncorrect);
		add(inputBox);
		add(repeatWord);
		add(submitWord);
		add(wordCountLabel);
		add(feedbackPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		inputBox.grabFocus();
		Object source = e.getSource();
		if (source == repeatWord)
			sayMessage(_testingWords.get(_wordNumber));
		if ((source == submitWord || source == inputBox) && submitWord.isEnabled())
			checkWord(inputBox.getText());
		if (source == menuButton) {
			int option = JOptionPane.showConfirmDialog(this, "If you go to the menu you will lose your progress in your current quiz. \nAre you sure you want to go to the menu?", "Are You Sure?", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
				spellingAid.returnToMenu();
		}
	}

	/**
	 * Checks if the word was spelled correctly.
	 * Gives order to update stats based on successfulness.
	 * Updates GUI based on progress.
	 * Gives order to perform appropriate level completion action.
	 * @param input
	 */
	protected void checkWord(String input) {
		inputBox.setText("");
		String word = _testingWords.get(_wordNumber);
		String festivalMessage;

		boolean correct = input.equalsIgnoreCase(word);

		if (correct) {
			festivalMessage = "Correct:";
			correctIncorrect.setIcon(CORRECT);
			_wordsCorrect++;
		} else {
			festivalMessage = "Incorrect:";
			correctIncorrect.setIcon(INCORRECT);
			if (_firstAttempt) {
				// FIRST FAIL
				_firstAttempt = false;
				sayMessage(festivalMessage+"The word is " + word + ":.:" + word);
				return;
			} 
		}
		FileAccess.updateStats(correct, word, _wordlist);

		_firstAttempt = true;
		_wordNumber++;
		feedbackPanel.setText(_wordsCorrect + " out of " + _wordNumber + " correct so far");

		// If all words have been tested:
		if (_wordNumber == _testingWords.size()) {
			sayMessage(festivalMessage);
			endQuiz();
		} else {
			// Test next word
			sayMessage(festivalMessage + "Spell " + _testingWords.get(_wordNumber));
			wordCountLabel.setText("Word " + (_wordNumber+1) +" of " + _testingWords.size());
		}
	}

	/**
	 * Starts the quiz with the specified WordList.
	 * @param w - The {@link WordList} to test the user on
	 */
	public void startQuiz(WordList w) {
		_wordlist = w;
		heading.setText(_wordlist.category());
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

	/**
	 * Makes a text to speech call with Festival.
	 * @param message - A string representing the message for Festival to say
	 */
	private void sayMessage(String message) {
		setButtonsEnabled(false);
		(new Festival(message, this)).execute();
	}

	/**
	 * Enables and disables submit and repeat buttons.
	 * Used to stop Festival from overlapping.
	 * @param b - true to enable the buttons, false otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		Color c = Color.GRAY;
		if (b)
			c = Color.WHITE;
		repeatWord.setBackground(c);
		submitWord.setBackground(c);
		repeatWord.setEnabled(b);
		submitWord.setEnabled(b);
	}

	/**
	 * Decide what to do on quiz end.
	 */
	protected void endQuiz() {
		// If the word list is 1 or 2 words long, the user needs to get full marks to 'pass'
		// Otherwise they need to get at most one word wrong.
		String[] options = {"Repeat Level", "Play Victory Video", "Return to Main Menu"};
		String message;
		String heading;
		if ((_wordsCorrect == _testingWords.size()-1 && _testingWords.size() > 2) 
				|| _wordsCorrect == _testingWords.size()) {
			message = "You have completed this level!\nWhat would you like to do?";
			heading = "Congratulations!";
		} else {
			message = "You didn't complete the level.\nTo complete a level, you must get at most one word incorrect. "
					+ "What would you like to do?";
			heading = "Unfortunate my friend";
			options = new String[] {options[0], options[2]};
		}
		endQuizOptions(message, heading, options, options.length-1);
	}
	
	/**
	 * Gives user options as level ends.
	 * @param message - the message to display in the JOptionPane
	 * @param heading - the heading to display in the JOptionPane
	 * @param options - the options to display in the JOptionPane
	 * @param menuOption - index of the option which returns the user to the menu
	 */
	protected void endQuizOptions(String message, String heading, String[] options, int menuOption) {
		// Give option for video reward.
		int option = JOptionPane.showOptionDialog(this, message, heading, JOptionPane.DEFAULT_OPTION, 
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == menuOption || option == JOptionPane.CLOSED_OPTION){
			spellingAid.returnToMenu();
		} else if (option == 0) {
			startQuiz(_wordlist);
		} else {
			selectFilterAndPlay();
			options = new String[] {options[0], options[2]};
			endQuizOptions(message, heading, options, 1);
		}
	}

	/**
	 * Gives user choice of videos filter, then plays video reward.
	 */
	private void selectFilterAndPlay() {
		if (!videoExists()) {
			JOptionPane.showMessageDialog(this, Video.NORMAL+" does not exist within "+ClassLoader.getSystemClassLoader().getResource(".").getPath()+"\n"
					+ "If you ran VOXSPELL without using the runVoxspell.sh script, then "+Video.NORMAL+" doesn't exist within your home directory\n"
					+ "Please make sure this file exists in the correct directory as to enable the video to be played");
			return;
		}
		String[] options = new String[] {"Normal","Negative"};
		int option = JOptionPane.showOptionDialog(this, "Select a filter", "Video Filter",
				JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			new BestMediaPlayer(Video.NORMAL);
		} else {
			new BestMediaPlayer(Video.NEGATIVE);
		}
	}

	/**
	 * @return true if video reward file exists
	 */
	private boolean videoExists() {
		return new File(Video.NORMAL.toString()).exists();
	}

	public String cardName() {
		return "Quiz";
	}
}