package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Stats extends Card implements ActionListener {
	
	private JScrollPane scrollPane;
	private JTable statsTable;
	private JButton backToMenu;
	
	public Stats(SpellingAid sp) throws FileNotFoundException {
		super(sp, "Spelling Statistics");
		
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		statsTable = new JTable();
		statsTable.setShowGrid(true);
		setLayout(null);

		add(backToMenu);

		int lineCount = 0;
		Scanner scanCount = new Scanner(new File(".history/all"));
		while (scanCount.hasNextLine()) {
			lineCount++;
			scanCount.nextLine();
		}
		scanCount.close();

		String[] tableHeadings = {"Word", "Times Mastered", "Times Faulted", "Time Failed"};
		String[][] tableContents = new String[lineCount][4];
		Scanner sc = new Scanner(new File(".history/all"));
		for (int i = 0; i < lineCount; i++) {
			for (int j = 0; j < 4; j++) {
				tableContents[i][j] = sc.next();
			}
		}
		sc.close();

		statsTable = new JTable(tableContents, tableHeadings);
		statsTable.setEnabled(false);
		statsTable.setAutoCreateRowSorter(true);

		scrollPane = new JScrollPane(statsTable);
		scrollPane.setBounds(0, 60, 500, 320);
		add(scrollPane);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == backToMenu) {
			spellingAid.returnToMenu();
		}
	}

}
