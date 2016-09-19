package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {
		
	private JButton backToMenu;
	public JButton clearStatistics;

	public Settings(SpellingAid sp) {
		super(sp, "Settings");
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.addActionListener(this);
		
		add(backToMenu);
		add(clearStatistics);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		} else if (e.getSource() == clearStatistics) {
			clearStats();
		}
	}
	
	private static void clearStats() {
		JFrame popupFrame = new JFrame();
		String message = "This will permanently delete all of your spelling history.\n"
				+ "Are you sure you want to do this?";
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Are you sure?", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION) {
			File[] folders = new File(".history").listFiles();
			for (File levelFolder : folders) {
				File[] files = levelFolder.listFiles();
				for (File file : files) {
					file.delete();
				}
			}
			new File(".history").delete();
			SpellingAid.createStatsFiles();
		}
	}


}
