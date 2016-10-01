package voxspell.cards;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

@SuppressWarnings("serial")
public class StatsTab extends JPanel {

	private JScrollPane scrollPane;
	private JTable statsTable;

	public StatsTab(int level) throws FileNotFoundException {
		statsTable = new JTable();
		statsTable.setShowGrid(true);
		setLayout(null);

		int lineCount = 0;
		Scanner scanCount = new Scanner(new File(".history/level"+level+"/stats"));
		while (scanCount.hasNextLine()) {
			lineCount++;
			scanCount.nextLine();
		}
		scanCount.close();

		String[] tableHeadings = {"Word", "Times Mastered", "Times Faulted", "Times Failed"};
		String[][] tableContents = new String[lineCount][4];
		Scanner sc = new Scanner(new File(".history/level"+level+"/stats"));
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
		scrollPane.setBounds(0, 0, 440, 290);
		add(scrollPane);

	}
}
