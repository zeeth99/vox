package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * GUI model for all main screens.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public abstract class Card extends JPanel implements ActionListener {
	protected SpellingAid spellingAid;

	protected JLabel heading;
	protected JButton menuButton;

	/**
	 * Creates the default screen GUI
	 * @param sp - The SpellingAid that created this
	 * @param str - The text for the heading label
	 */
	public Card(SpellingAid sp, String str) {
		spellingAid = sp;

		setLayout(null);

		heading = new JLabel(str);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 20));
		heading.setBounds(0, 0, 500, 60);
		menuButton = new JButton("Menu");
		menuButton.setBounds(12, 15, 73, 30);
		menuButton.addActionListener(this);

		add(menuButton);

		add(heading);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// Returns the user to the main menu
		if (e.getSource() == menuButton)
			spellingAid.returnToMenu();
	}

	/**
	 * @return A String representing the card subclass
	 */
	public abstract String cardName();

}
