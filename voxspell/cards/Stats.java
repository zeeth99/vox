package voxspell.cards;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class Stats extends JPanel implements ActionListener {
	private SpellingAid spellingAid;
	
	private JLabel statsLabel;
	private JScrollPane scrollPane;
	private JTable statsTable;
	private JButton backToMenu;
	
	public Stats(SpellingAid sp) throws FileNotFoundException {
		spellingAid = sp;
		
		statsLabel = new JLabel("Spelling Statistics");
		statsLabel.setHorizontalAlignment(SwingConstants.CENTER);
		statsLabel.setFont(new Font("Tibetan Machine Uni", Font.BOLD, 20));
		statsLabel.setBounds(0, 0, 500, 60);
		backToMenu = new JButton("Menu");
		backToMenu.setBounds(12, 18, 73, 25);
		backToMenu.addActionListener(this);
		statsTable = new JTable();
		statsTable.setShowGrid(true);
		setLayout(null);

		add(backToMenu);
		add(statsLabel);

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
