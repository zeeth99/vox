package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import voxspell.SpellingAid;

public class Settings extends Card implements ActionListener {
	private SpellingAid spellingAid;
	
	
	public Settings(SpellingAid sp) {
		super(sp, "Settings");
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
