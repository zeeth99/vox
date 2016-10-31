package voxspell;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import voxspell.resource.FileAccess;

import javax.swing.UnsupportedLookAndFeelException;

/**
 * Main class for VOXSPELL. Handles the frame.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class SpellingAid extends JFrame {

	/**
	 * Set up the program.
	 * @param args
	 */
	private SpellingAid() {
		// Set LookAndFeel to Nimbus. If Nimbus isn't installed, set Look and Feel to
		// system Look and Feel.
		String LookAndFeel = UIManager.getSystemLookAndFeelClassName();
		for (LookAndFeelInfo LaF : UIManager.getInstalledLookAndFeels())
			if (LaF.getName().equals("Nimbus"))
				LookAndFeel = LaF.getClassName();
		try {
			UIManager.setLookAndFeel(LookAndFeel);
		} catch (ClassNotFoundException | InstantiationException 
				| IllegalAccessException | UnsupportedLookAndFeelException e) {}

		setResizable(false);
		setTitle("VOXSPELL");
		setSize(500, 400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);

		// Set up important files
		FileAccess.createFiles();
		
		setContentPane(new CardManager());
		setVisible(true);
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new SpellingAid();
			}
		});
	}
}
