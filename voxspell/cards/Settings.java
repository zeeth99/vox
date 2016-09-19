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
import javax.swing.JOptionPane;

import voxspell.Festival;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {
	
	public static final String DEFAULT_VOICE = "(voice_kal_diphone)"; 
	public static final String NZ_VOICE = "(voice_akl_nz_jdt_diphone)";
	  
	private JButton backToMenu;
	public JButton clearStatistics;
	private JComboBox<String> voiceSettings;

	public Settings(SpellingAid sp) {
		super(sp, "Settings");
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.addActionListener(this);
		
		String[] voices = new String[] {"Default","New Zealand"};
		voiceSettings = new JComboBox<String>(voices);
		voiceSettings.setSelectedIndex(0);
		voiceSettings.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				JComboBox combo = (JComboBox)e.getSource();
                String currentQuantity = (String)combo.getSelectedItem();
                switch (currentQuantity) {
                case "Default":
                	changeVoiceSetting(DEFAULT_VOICE);
                	break;
                case "New Zealand":
                	changeVoiceSetting(NZ_VOICE);
                default:
                	break;
                }
            }
          });
		
		add(backToMenu);
		add(clearStatistics);
		add(voiceSettings);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		} else if (e.getSource() == clearStatistics) {
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
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			File[] files = new File(".history").listFiles();
			for (int i = 0; i < files.length; i++) {
				files[i].delete();
			}
			new File(".history").delete();
			SpellingAid.createStatsFiles();
		}
	}


}
