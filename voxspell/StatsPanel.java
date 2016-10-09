package voxspell;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

@SuppressWarnings("serial")
public class StatsPanel extends JScrollPane implements ListSelectionListener {
	private Stats _stats;


	public StatsPanel(Stats stats) {
//		_stats = stats;
//		int lineCount = 0;
//		Scanner scanCount = new Scanner(new File(".history/level"+level+"/stats"));
//		while (scanCount.hasNextLine()) {
//			lineCount++;
//			scanCount.nextLine();
//		}
//		scanCount.close();
//
//		String[] tableHeadings = {"Word", "Times Mastered", "Times Faulted", "Times Failed"};
//		String[][] tableContents = new String[lineCount][4];
//		Scanner sc = new Scanner(new File(".history/level"+level+"/stats"));
//		for (int i = 0; i < lineCount; i++) {
//			for (int j = 0; j < 4; j++) {
//				tableContents[i][j] = sc.next();
//			}
//		}
//		sc.close();
//
//		statsTable = new JTable(tableContents, tableHeadings);
//		statsTable.setShowGrid(true);
//		statsTable.setEnabled(false);
//		statsTable.setAutoCreateRowSorter(true);
//
//		scrollPane = new JScrollPane(statsTable);
//		scrollPane.setBounds(0, 0, 440, 290);
//		add(scrollPane);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

		revalidate();
		repaint();
	}
}
