package quiz;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class BestQuizOCE extends JFrame implements ActionListener {

	private JButton btnNew = new JButton("New Spelling Quiz");
	private JButton btnReview = new JButton("Review Mistakes");
	private JButton btnViewStats = new JButton("View Statistics");
	private JButton btnClearStats = new JButton("Clear Statistics");
	
	private List<JButton> menuButtonList = new ArrayList<>();
	
	private JPanel menuPanel = new JPanel();
	
	private JTextField quizInput = new JTextField(20);
	private JLabel quizLabel = new JLabel();
	private JButton btnQSubmit = new JButton("Submit");
	private JPanel quizTopPanel = new JPanel();
	private JPanel quizBotPanel = new JPanel();
	private JPanel quizPanel = new JPanel();
	
	private JTextField reviewInput = new JTextField(20);
	private JLabel reviewLabel = new JLabel();
	private JButton btnRSubmit = new JButton("Submit");
	private JPanel reviewTopPanel = new JPanel();
	private JPanel reviewBotPanel = new JPanel();
	private JPanel reviewPanel = new JPanel();
	
	private JTextArea viewStats = new JTextArea(10,40);
	private JLabel sortOrderLabel = new JLabel("Words Sorted Alphabetically");
	private JButton btnReturn = new JButton("Return to Main Menu");
	private JPanel statsPanel = new JPanel();
	
	private JPanel centre = new JPanel();
	
	private static int quizWordCount = 1;
	private static String wordBeingQuizd = null;
	private static int tryCount = 1;
	private static int wordsToReview;
	
	private FileOperation ops;
	
	public BestQuizOCE() {
		super("BEST QUIZ OCE");
		
		ops = new FileOperation(this);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		/* Following code retrieved and slightly modified from http://stackoverflow.com/questions/21330682/confirmation-before-press-yes-to-exit-program-in-java*/ 
		addWindowListener(new WindowAdapter() {
			  public void windowClosing(WindowEvent e) {
			    int option = JOptionPane.showConfirmDialog(null, 
			        "Are you sure you want to quit?", "Why so soon?",
			        JOptionPane.YES_NO_OPTION);

			    if (option == JOptionPane.YES_OPTION) {
			    	System.exit(0);
			    }
			  }
		});
		
		
		menuButtonList.add(btnNew);
		menuButtonList.add(btnReview);
		menuButtonList.add(btnViewStats);
		menuButtonList.add(btnClearStats);
		
		setLayout(new BorderLayout());
		menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.X_AXIS));
		for (JButton b : menuButtonList) {
			b.addActionListener(this);
			menuPanel.add(b);
		}
		btnQSubmit.addActionListener(this);
		btnRSubmit.addActionListener(this);
		btnReturn.addActionListener(this);
		
		setUpQuizPanel();
		setUpReviewPanel();
		setUpStatsPanel();
		
		centre.add(quizPanel);
		centre.add(reviewPanel);
		centre.add(statsPanel);
		
		// JScrollPane scroll = new JScrollPane(viewStats);
		
		add(menuPanel, BorderLayout.NORTH);
		add(centre, BorderLayout.CENTER);
		menuPanel.setVisible(true);
		centre.setVisible(true);
		quizPanel.setVisible(false);
		reviewPanel.setVisible(false);
		statsPanel.setVisible(false);
		pack();
	}
	
	private void setUpQuizPanel() {
		quizTopPanel.add(quizInput, BoxLayout.X_AXIS);
		quizTopPanel.add(quizLabel, BoxLayout.X_AXIS);
		quizBotPanel.add(btnQSubmit, BoxLayout.X_AXIS);
		quizPanel.setPreferredSize(new Dimension(500,100));
		quizPanel.setLayout(new BoxLayout(quizPanel, BoxLayout.Y_AXIS));
		quizPanel.add(quizTopPanel);
		quizPanel.add(quizBotPanel);
	}
	
	private void setUpReviewPanel() {
		reviewTopPanel.add(reviewInput, BoxLayout.X_AXIS);
		reviewTopPanel.add(reviewLabel, BoxLayout.X_AXIS);
		reviewBotPanel.add(btnRSubmit, BoxLayout.X_AXIS);
		reviewPanel.setPreferredSize(new Dimension(500,100));
		reviewPanel.setLayout(new BoxLayout(reviewPanel, BoxLayout.Y_AXIS));
		reviewPanel.add(reviewTopPanel);
		reviewPanel.add(reviewBotPanel);
	}
	
	private void setUpStatsPanel() {
		statsPanel.setPreferredSize(new Dimension(600,250));
		JScrollPane scroll = new JScrollPane(viewStats);
		statsPanel.add(sortOrderLabel, BorderLayout.NORTH);
		statsPanel.add(scroll, BorderLayout.CENTER);
		statsPanel.add(btnReturn, BorderLayout.SOUTH);
	}
	
	@Override
	public void actionPerformed(ActionEvent ae) {
		if (ae.getSource() == btnNew) {
			menuPanel.setVisible(false);
			quizPanel.setVisible(true);
			pack();
			wordBeingQuizd = ops.getRandomWordFromWordList();
			sayQuizWord();
		} else if (ae.getSource() == btnReview) {
			wordsToReview = ops.maxNumberOfWordsToSpell();
			
			wordBeingQuizd = ops.getRandomWordFromReview();
			if (wordBeingQuizd == null | wordsToReview == -1) {
				JOptionPane.showMessageDialog(this, "No words to review");
				menuPanel.setVisible(true);
				reviewPanel.setVisible(false);
				pack();
				return;
			}
			menuPanel.setVisible(false);
			reviewPanel.setVisible(true);
			pack();
			sayReviewWord();
		} else if (ae.getSource() == btnViewStats) {
			List<String> quizdWords = ops.getQuizdWords();
			if (quizdWords.isEmpty()) {
				JOptionPane.showMessageDialog(this, "No statistics to view");
				return;
			}
			menuPanel.setVisible(false);
			statsPanel.setVisible(true);
			pack();
			Collections.sort(quizdWords);
			for (String s : quizdWords) {
				viewStats.append(s+"\n");
				viewStats.append("       Mastered: "+ops.numberOfTimesMastered(s)+"\n");
				viewStats.append("       Faulted: "+ops.numberOfTimesFaulted(s)+"\n");
				viewStats.append("       Failed: "+ops.numberOfTimesFailed(s)+"\n");
			}
		} else if (ae.getSource() == btnReturn) {
			viewStats.setText("");
			statsPanel.setVisible(false);
			menuPanel.setVisible(true);
			pack();
		} else if (ae.getSource() == btnClearStats) {
			int option = JOptionPane.showConfirmDialog(this, "You sure you want to do that friend?", 
					"Select an option", JOptionPane.YES_NO_OPTION);
			if (option == JOptionPane.YES_OPTION) {
				ops.clearStats();
				JOptionPane.showMessageDialog(this, "Statistics have been cleared");
			}
			return;
		} else if (ae.getSource() == btnQSubmit) { // New Spelling Quiz
			ops.addWordToQuizd(wordBeingQuizd);
			
			if (!isValidInput(quizInput)) {
				if (quizInput.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "At least attempt to spell the word");
				} else {
					JOptionPane.showMessageDialog(this, "Invalid Input\nPlease check there are no leading or trailing spaces"
							+ "\nand that there are no numbers or any special characters.");
				}
				return;
			}
			
			// Detects how many tries the user has had at a given word
			// If they fail once, it returns without executing code which would give
			// a new word and on the second failure, it moves on to the next word
			if (tryCount == 1) {
				if (isWordCorrect(wordBeingQuizd, quizInput.getText())) {
					sayCorrect();
					ops.addWordToMastered(wordBeingQuizd);
				} else {
					sayTryAgain();
					tryCount++;
					quizInput.setText("");
					return;
				}
			} else if (tryCount == 2) {
				if (isWordCorrect(wordBeingQuizd, quizInput.getText())) {
					sayCorrect();
					ops.addWordToFaulted(wordBeingQuizd);
				} else {
					sayIncorrect();
					ops.addWordToFailed(wordBeingQuizd); // Used to track failed statistic
					ops.addWordToReview(wordBeingQuizd); // Word available in Review Mistakes option
				}
				tryCount = 1;
			}
			quizWordCount++;
			
			// If number of quizzed words reaches 3. Exits automatically back to main menu
			if (quizWordCount > 3) {
				quizWordCount = 1;
				menuPanel.setVisible(true);
				quizPanel.setVisible(false);
				pack();
			} else {
				wordBeingQuizd = ops.getRandomWordFromWordList();
				ops.addWordToQuizd(wordBeingQuizd);
				sayQuizWord();
			}
			quizInput.setText("");
		} else if (ae.getSource() == btnRSubmit) { // Review 
			
			if (!isValidInput(reviewInput)) {
				if (reviewInput.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "At least attempt to spell the word");
				} else {
					JOptionPane.showMessageDialog(this, "Invalid Input\nPlease check there are no leading or trailing spaces"
							+ "\nand that there are no numbers or any special characters.");
				}
				return;
			}
			
			if (tryCount == 1) {
				if (isWordCorrect(wordBeingQuizd, reviewInput.getText())) {
					sayCorrect();
					ops.addWordToMastered(wordBeingQuizd);
					ops.removeFromReview(wordBeingQuizd);
				} else {
					sayTryAgain();
					tryCount++;
					reviewInput.setText("");
					return;
				}
			} else if (tryCount == 2) {
				if (isWordCorrect(wordBeingQuizd, reviewInput.getText())) {
					sayCorrect();
					ops.addWordToFaulted(wordBeingQuizd);
					ops.removeFromReview(wordBeingQuizd);
					tryCount = 1;
				} else {
					sayIncorrect();
					ops.addWordToFailed(wordBeingQuizd);
					// Gives option to hear word spelt out
					int option = JOptionPane.showConfirmDialog(this, "Would you like to hear the word spelt out?", 
							"Select an option", JOptionPane.YES_NO_OPTION);
				
					if (option == JOptionPane.YES_OPTION) {
						spellWordOut();
						tryCount++;
						return;
					}
				}
			} else if (tryCount == 3) {
				if (isWordCorrect(wordBeingQuizd, reviewInput.getText())) {
					sayCorrect();
					ops.removeFromReview(wordBeingQuizd);
				} else {
					sayIncorrect();
					ops.addWordToFailed(wordBeingQuizd);
				}
				tryCount = 1;
			}
			quizWordCount++;
			
			if (quizWordCount > wordsToReview) {
				quizWordCount = 1;
				menuPanel.setVisible(true);
				reviewPanel.setVisible(false);
				pack();
			} else {
				wordBeingQuizd = ops.getRandomWordFromReview();
				ops.addWordToQuizd(wordBeingQuizd);
				sayReviewWord();
			}
			reviewInput.setText("");
		}
		
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				BestQuizOCE bco = new BestQuizOCE();
				bco.setVisible(true);
			}
		});
	}
	
	// TTS says Correct
	private void sayCorrect() {
		String cmd = "echo \"Correct\" | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {}
		
	}
	
	// TTS says Incorrect and prompts them to try again
	private void sayTryAgain() {
		String cmd = "echo \"Incorrect, the word is "+wordBeingQuizd+".. "+wordBeingQuizd+"\" | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {}
		
	}
	
	// TTS says Incorrect for when user fails to spell word twice
	private void sayIncorrect() {
		String cmd = "echo \"Incorrect.\" | festival --tts";
		ProcessBuilder builder = new ProcessBuilder("/bin/bash","-c",cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {}
		
	}
	
	// TTS prompts user to spell the word given as the parameter
	private void sayQuizWord() {
		String cmd = "echo \"Please spell "+ wordBeingQuizd+"\" | festival --tts";
		quizLabel.setText("Spell word "+quizWordCount+" of 3: ");
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {}
		
	}
	
	private void spellWordOut() {
		String[] splitWord = wordBeingQuizd.split("");
		String cmd;
		cmd = "echo \"The word is spelt.. \" | festival --tts";
		ProcessBuilder builder;
		builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) {}
		
		for (String s : splitWord) {
			cmd = "echo \""+s+".\" | festival --tts";
			builder = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = builder.start();
				process.waitFor();
			} catch (IOException | InterruptedException e) {}
		}
	}
	
	
	private void sayReviewWord() {
		String cmd = "echo \"Please spell "+ wordBeingQuizd+"\" | festival --tts";
		reviewLabel.setText("Spell word "+quizWordCount+" of "+wordsToReview+": ");
		ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = builder.start();
			process.waitFor();
		} catch (IOException | InterruptedException e) { }
		
	}
	
	// Checks if two words are equal (case insensitive)
	private boolean isWordCorrect(String wordToSpell, String wordInput) {
		return wordToSpell.toLowerCase().equals(wordInput.toLowerCase());
		
	}
	
	private boolean isValidInput(JTextField input) {
		/* Code retrived and slighlty modified from http://stackoverflow.com/questions/5238491/check-if-string-contains-only-letters*/
		return input.getText().matches("[a-zA-Z]+");
		
	}
	
}
