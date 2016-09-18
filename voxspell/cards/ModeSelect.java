package voxspell.cards;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ModeSelect extends Card implements ActionListener{

	private JButton[] levels = new JButton[11];
	public JButton reviewButton;

	public ModeSelect(SpellingAid sp) {
		super(sp, "Select Your Mode");
		
		int[] levelButtonsX = {50, 190, 330};
		int[] levelButtonsY = {70, 120, 170, 220};
		
		for (int i = 0; i < 11; i++) {
			levels[i] = new JButton("Level " + String.valueOf(i+1));
			levels[i].setBounds(levelButtonsX[i%3], levelButtonsY[(int) (i / 3)], 120, 30);
			add(levels[i]);
			levels[i].addActionListener(this);
		}
		
		reviewButton = new JButton("Review");
		reviewButton.setBackground(Color.GRAY); //TODO: Looks crap. 
												// I think it needs to stand out a little, 
												// but I'm not sure how to do it well.
		reviewButton.setBounds(330, 220, 120, 30);
		
		add(reviewButton);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == reviewButton) {
			
		} else {
			for (int i = 0; i < 11; i++) {
				if (e.getSource() == levels[i]) {
					spellingAid.startQuiz(i+1);
				}
			}
		}
	}
}
