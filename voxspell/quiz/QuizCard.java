package voxspell.quiz;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.Card;
import voxspell.CardManager;
import voxspell.resource.FileAccess;

/**
 * Screen to test the user on a list of up to 10 words.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class QuizCard extends Card {

	public Quizzer quizzer;
	public Reviewer reviewer;
	
	public static final ImageIcon CORRECT = new ImageIcon(FileAccess.getMedia("Correct.png"));
	public static final ImageIcon INCORRECT = new ImageIcon(FileAccess.getMedia("Incorrect.png"));

	private JLabel wordCountLabel;
	private JFormattedTextField inputBox;
	private JLabel correctIncorrect;
	private JLabel feedbackPanel;
	private JButton repeatWord;
	private JButton submitWord;

	/**
	 * Set up the GUI
	 * @param cm - The CardManager that created this
	 */
	public QuizCard(CardManager cm) {
		super(cm, "New Quiz");
				
		wordCountLabel = new JLabel("Word 0 out of 0");
		wordCountLabel.setBounds(125, 90, 150, 15);
		feedbackPanel = new JLabel("0 out of 0 correct");
		feedbackPanel.setBounds(125, 230, 300, 15);

		// Button used to hear the word to spell again
		repeatWord = new JButton("Repeat");
		repeatWord.setBounds(135, 175, 85, 25);
		repeatWord.setToolTipText("<html>Hear the word again.<br>Alternate: Ctrl-Space</html>");
		repeatWord.setFocusable(false);

		// Button to submit a proposed spelling of the word
		submitWord = new JButton("Submit");
		submitWord.setBounds(280, 175, 85, 25);
		submitWord.setToolTipText("<html>Submit spelling attempt.<br>Alternate: Enter</html>");
		submitWord.setFocusable(false);

		// Box to type the word to spell
		inputBox = new JFormattedTextField();
		inputBox.setBounds(125, 120, 250, 30);
		inputBox.setFont(new Font("Dialog", Font.PLAIN, 16));
		inputBox.setToolTipText("Type here.");
		inputBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				submitWord.doClick();
			}
		});
		inputBox.addKeyListener(new KeyAdapter(){ // Only letters and apostrophes can be inputed
			@Override
			public void keyTyped(KeyEvent e){
				// ctrl-space repeats word.
				char ch = e.getKeyChar();
//				if (e.getKeyCode() == KeyEvent.VK_ENTER)
//					submitWord.doClick();
				if (e.isControlDown() && ch == ' ')
					repeatWord.doClick();
				if(!(Character.isLetter(ch) || ch=='\'' || ch==' ' || ch=='-'))
					e.consume();
			}
		});
		setDefaultFocusComponent(inputBox);

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

	/**
	 * Enables and disables submit and repeat buttons.
	 * Used to stop Festival from overlapping.
	 * @param b - true to enable the buttons, false otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		repeatWord.setEnabled(b);
		submitWord.setEnabled(b);
	}

	public void start(String category, int size) {
		heading.setText(category);
		wordCountLabel.setText("Word 0 of " + size);
		feedbackPanel.setText("0 out of 0 correct so far");
	}
	
	public void update(boolean b) {
		String[] feedback = feedbackPanel.getText().split(" ");
		int correct = Integer.parseInt(feedback[0]);
		if (b) correct++;
		int attempted = Integer.parseInt(feedback[3]) + 1;
		feedbackPanel.setText(correct + " out of " + attempted + " correct so far");
		
		String[] wordCount = wordCountLabel.getText().split(" ");
		int current = attempted;
		int size = Integer.parseInt(wordCount[3]);
		if (current != size) current++;
			wordCountLabel.setText("Word " + current +" of " + size);

		if (b) correctIncorrect.setIcon(CORRECT);
		else correctIncorrect.setIcon(INCORRECT);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == menuButton) {
			int option = JOptionPane.showConfirmDialog(this, "If you go to the menu you will lose your progress in your current quiz. \nAre you sure you want to go to the menu?", "Are You Sure?", JOptionPane.OK_CANCEL_OPTION);
			if (option == JOptionPane.OK_OPTION)
				super.actionPerformed(e);
		}
	}

	public void setReviewMode(boolean b) {
		repeatWord.removeActionListener(quizzer);
		submitWord.removeActionListener(quizzer);
		if (b)
			quizzer = new Reviewer(this);
		else
			quizzer = new Quizzer(this);
		repeatWord.addActionListener(quizzer);
		submitWord.addActionListener(quizzer);
	}
	
	@Override
	public void onCardShown() { quizzer.startQuiz(); }
	
	public JButton getRepeatButton() { return repeatWord; }
	
	public JButton getSubmitButton() { return submitWord; }
	
	public String getText() { return inputBox.getText(); }
	
	public void clearInputBox() { inputBox.setText(""); }
	
	@Override
	public String cardName() { return "Quiz"; }
	
	
}
