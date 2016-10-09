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

	private JScrollPane scrollPane;
	private JList<StatsList> list;
	private StatsPanel statsPanel;
	private JSplitPane splitPane;

	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");

		splitPane = new JSplitPane();
		splitPane.setBounds(15, 55, 465, 290);
		add(splitPane);

		DefaultListModel<StatsList> listModel = new DefaultListModel<StatsList>();
		addFolderToList(SpellingAid.STATSFOLDER, listModel);

		list = new JList<StatsList>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(statsPanel);

		scrollPane = new JScrollPane();
		scrollPane.setViewportView(list);
		splitPane.setLeftComponent(scrollPane);

		statsPanel = new StatsPanel(this);
		splitPane.setRightComponent(statsPanel);
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
