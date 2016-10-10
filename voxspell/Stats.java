package voxspell;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.quiz.StatsList;
import javax.swing.JSplitPane;

@SuppressWarnings("serial")
public class Stats extends Card {

	private JList<StatsList> list;
	private StatsPanel statsPanel;
	private JSplitPane splitPane;

	public Stats(SpellingAid sp) {
		// Set up GUI
		super(sp, "Spelling Statistics");

		statsPanel = new StatsPanel();

		list = new JList<StatsList>(new DefaultListModel<StatsList>());
		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		list.addListSelectionListener(statsPanel);
		addFolderToList(SpellingAid.STATSFOLDER, (DefaultListModel<StatsList>)list.getModel());

		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), statsPanel);
		splitPane.setBounds(15, 55, 465, 290);
		splitPane.setDividerLocation(120);
		add(splitPane);
	}

	private void addFolderToList(File d, DefaultListModel<StatsList> listModel) {
		for (File f : d.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(new StatsList(d, s.substring(0, s.length()-6)));
			}
		}
	}

	public String cardName() {
		return "Statistics";
	}
}
