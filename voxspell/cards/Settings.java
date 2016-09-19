package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;

import voxspell.Festival;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {
	
	public static final String DEFAULT_VOICE = "(voice_kal_diphone)";
	public static final String NZ_VOICE = "(voice_akl_nz_jdt_diphone)";
	
	private JButton backToMenu;
	private JButton changeVoice;

	public Settings(SpellingAid sp) {
		super(sp, "Settings");
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		
		changeVoice = new JButton("Change Voice");
		changeVoice.addActionListener(this);
		changeVoice.setBounds(100,100,150,100);
		
		add(backToMenu);
		add(changeVoice);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		} else if (e.getSource() == changeVoice) {
			changeVoiceSetting();
		}
	}
	
	private void changeVoiceSetting() {
		try {
			List<String> lines = Files.readAllLines(Festival.SCHEME_FILE.toPath());
			lines.set(0, NZ_VOICE);
			Files.write(Festival.SCHEME_FILE.toPath(), lines);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
