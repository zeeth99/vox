package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public abstract class Card extends JPanel implements ActionListener {
	protected SpellingAid spellingAid;
	
	private JLabel heading;
	
	public Card(SpellingAid sp, String str) {
		spellingAid = sp;
	
		setLayout(null);
		
		heading = new JLabel(str);
		heading.setHorizontalAlignment(SwingConstants.CENTER);
		heading.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		heading.setBounds(0, 0, 500, 60);
		
		add(heading);
	}

}
