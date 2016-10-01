package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public abstract class Card extends JPanel implements ActionListener {
	protected SpellingAid spellingAid;
	
	protected JLabel heading;
	protected JButton menuButton;
	
	public Card(SpellingAid sp, String str) {
		spellingAid = sp;
	
		setLayout(null);
		
		heading = new JLabel(str);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		heading.setBounds(0, 0, 500, 60);
		menuButton = new JButton("Menu");
		menuButton.setBounds(12, 18, 73, 25);
		menuButton.addActionListener(this);
		
		add(menuButton);
		
		add(heading);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == menuButton) {
			spellingAid.returnToMenu();
		}
	}
	
	public abstract String cardName();

}
