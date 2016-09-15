package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {
	private SpellingAid spellingAid;
		
	private JButton backToMenu;

	public Settings(SpellingAid sp) {
		super(sp, "Settings");
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		
		add(backToMenu);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		}
	}

}
