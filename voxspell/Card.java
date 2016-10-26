package voxspell;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
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
	
	private JComponent defaultFocus;

	/**
	 * Creates the default screen GUI
	 * @param sp - The SpellingAid that created this
	 * @param str - The text for the heading label
	 */
	public Card(SpellingAid sp, String str) {
		spellingAid = sp;

		// Use the 'Esc' button to return to menu.
		getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "Esc");
		getActionMap().put("Esc", new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				menuButton.doClick();
			}
		});
		
		// Give focus to defaultFocus when the Card is shown.
		addComponentListener(new ComponentListener() {

			@Override
			public void componentHidden(ComponentEvent arg0) {}

			@Override
			public void componentMoved(ComponentEvent arg0) {}

			@Override
			public void componentResized(ComponentEvent arg0) {}

			@Override
			public void componentShown(ComponentEvent arg0) {
				if (defaultFocus != null)
					defaultFocus.requestFocusInWindow();
			}
			
		});

		setLayout(null);

		heading = new JLabel(str);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Sans Serif", Font.BOLD, 20));
		heading.setBounds(0, 0, 500, 60);
		menuButton = new JButton("Menu");
		menuButton.setBounds(12, 15, 73, 30);
		menuButton.setToolTipText("<html>Return to the menu.<br>Alternate: Esc</html>");
		menuButton.addActionListener(this);
		menuButton.setFocusable(false);


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
	 * Set the component which should be focused on when the card is displayed
	 * @param arg The JComponent to get the focus
	 */
	protected void setDefaultFocusComponent(JComponent arg) {
		defaultFocus = arg;
	}
	
	/**
	 * @return A String representing the card subclass
	 */
	public abstract String cardName();

}
