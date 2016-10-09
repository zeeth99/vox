package voxspell;

import java.io.File;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

@SuppressWarnings("serial")
public class Stats extends Card {

	private JScrollPane scrollPane;
	private JList<String> list;
	private JScrollPane scrollPane1;
	private StatsPanel statsPanel;
	
	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		addFolderToList(SpellingAid.STATSFOLDER, listModel);
		
		list = new JList<String>(listModel);
		list.setBounds(0, 0, 0, 0);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(statsPanel);
		add(list);
		
		scrollPane = new JScrollPane();
		scrollPane.setLocation(15, 55);
		scrollPane.setSize(130, 290);
		scrollPane.setViewportView(list);
		add(scrollPane);
		
		statsPanel = new StatsPanel();

		scrollPane1 = new JScrollPane();
		add(scrollPane1);
	}
	
	private void addFolderToList(File d, DefaultListModel<String> listModel) {
		for (File f : d.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(s.substring(0, s.length()-".stats".length()));
			}
		}
	}

	public String cardName() {
		return "Statistics";
	}
}
