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
public class LevelSelect extends JPanel implements ActionListener{
	SpellingAid spellingAid;

	private JLabel levelSelectLabel;
	private JButton[] levels = new JButton[11];


	public LevelSelect(SpellingAid sp) {
		spellingAid = sp;		
		
		levelSelectLabel = new JLabel("Select Your Quiz Level");
		levelSelectLabel.setHorizontalAlignment(SwingConstants.CENTER);
		levelSelectLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		levelSelectLabel.setBounds(0, 0, 500, 60);

		add(levelSelectLabel);
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < 11; i++) {
			if (e.getSource() == levels[i]) {
				spellingAid.startQuiz(i+1);
			}
		}
	}
}
