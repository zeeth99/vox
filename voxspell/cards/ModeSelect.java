package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ModeSelect extends Card implements ActionListener{

	private JButton[] levels = new JButton[11];

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
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() != menuButton) {
			for (int i = 0; i < 11; i++) {
				if (e.getSource() == levels[i]) {
					spellingAid.startQuiz(i+1);
				}
			}
		}
	}
	
}
