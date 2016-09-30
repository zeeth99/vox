package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.Festival;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {
	
	public static final String DEFAULT_VOICE = "(voice_kal_diphone)"; 
	public static final String NZ_VOICE = "(voice_akl_nz_jdt_diphone)";
	  
	public JButton clearStatistics;
	private JComboBox<String> voiceSettings;
	private JLabel voiceSettingsLabel;

	public Settings(SpellingAid sp) {
		super(sp, "Settings");

		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.addActionListener(this);
		
		String[] voices = new String[] {"Default","New Zealand"};
		voiceSettings = new JComboBox<String>(voices);
		voiceSettings.setSelectedIndex(0);
		voiceSettings.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>)e.getSource();
                String voice = (String)combo.getSelectedItem();
                switch (voice) {
                case "Default":
                	changeVoiceSetting(DEFAULT_VOICE);
                	break;
                case "New Zealand":
                	changeVoiceSetting(NZ_VOICE);
                	break;
                }
            }
          });
		
		voiceSettings.setBounds(100, 100, 300, 20);
		
		voiceSettingsLabel = new JLabel("Select a voice setting:");
		voiceSettingsLabel.setBounds(100, 80, 300, 20);
		
		add(clearStatistics);
		add(voiceSettings);
		add(voiceSettingsLabel);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == clearStatistics) {
			clearStats();
		}
	}
	
	private void changeVoiceSetting(String setting) { 
	    try { 
	      List<String> lines = Files.readAllLines(Festival.SCHEME_FILE.toPath()); 
	      lines.set(0, setting); 
	      Files.write(Festival.SCHEME_FILE.toPath(), lines); 
	       
	    } catch (Exception e) { 
	      e.printStackTrace();
	    }
	}
	
	private static void clearStats() {
		JFrame popupFrame = new JFrame();
		String message = "This will permanently delete all of your spelling history.\n"
				+ "Are you sure you want to do this?";
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Delete Statistics", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION)
			for (File f : SpellingAid.STATSFOLDER.listFiles())
				f.delete();
	}


}
