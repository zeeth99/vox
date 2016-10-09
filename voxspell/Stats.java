package voxspell;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.quiz.WordList;

@SuppressWarnings("serial")
public class Stats extends Card {

	private JScrollPane scrollPane;
	private JList<WordList> list;
	private JScrollPane scrollPane1;
	private StatsPanel statsPanel;
	
	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");
		
		DefaultListModel<WordList> listModel = new DefaultListModel<WordList>();
		addFolderToList(SpellingAid.STATSFOLDER, listModel);
		
		list = new JList<WordList>(listModel);
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
	
	private void addFolderToList(File d, DefaultListModel<WordList> listModel) {
		for (File f : d.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(new WordList(d, s.substring(0, s.length()-6)));
			}
		}
	}

	public String cardName() {
		return "Statistics";
	}
}
