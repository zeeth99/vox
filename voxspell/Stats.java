package voxspell;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.quiz.StatsList;

@SuppressWarnings("serial")
public class Stats extends Card {

	private JScrollPane scrollPane;
	private JList<StatsList> list;
	private JScrollPane scrollPane1;
	private StatsPanel statsPanel;
	
	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");
		
		DefaultListModel<StatsList> listModel = new DefaultListModel<StatsList>();
		addFolderToList(SpellingAid.STATSFOLDER, listModel);
		
		list = new JList<StatsList>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(statsPanel);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 55, 130, 290);
		scrollPane.setViewportView(list);
		add(scrollPane);
		
		statsPanel = new StatsPanel(this);

		scrollPane1 = new JScrollPane();
		add(scrollPane1);
	}
	
	private void addFolderToList(File d, DefaultListModel<StatsList> listModel) {
		for (File f : d.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(new StatsList(f));
			}
		}
	}

	public String cardName() {
		return "Statistics";
	}
}
