package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Stats extends Card implements ActionListener {
	
	private JTabbedPane tabbedPane;
	
	private JPanel[] tabs;
	
	private JButton backToMenu;
	
	public Stats(SpellingAid sp) throws FileNotFoundException {
		super(sp, "Spelling Statistics");
		
		// Make the JTabbedPane
		tabbedPane = new JTabbedPane();
		tabbedPane.setBounds(5, 50, 490, 300);
		tabbedPane.setTabPlacement(JTabbedPane.LEFT);
		tabbedPane.setFont(new Font(tabbedPane.getFont().getName(), Font.PLAIN, 19));
		
		// Create each tab and add it to tabbedPane
		tabs = new JPanel[11];
		for (int i = 0; i < 11; i++) {
			int level = i+1;
			tabs[i] = new StatsTab(level);
			tabbedPane.addTab(""+level, tabs[i]);
		}
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		
		add(backToMenu);
		add(tabbedPane);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		}
	}

}
