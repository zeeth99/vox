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
		
		statsPanel = new StatsPanel();
		
		scrollPane = new JScrollPane();
		scrollPane.setViewportView(statsPanel);
		add(scrollPane);
		
		DefaultListModel<String> listModel = new DefaultListModel<String>();
		for (String s : SpellingAid.STATSFOLDER.list())
			if (s.endsWith(".stats") && (new File(s)).length() > 0)
				listModel.addElement(s.substring(0, s.length()-".stats".length()));
		
		list = new JList<String>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.addListSelectionListener(statsPanel);
		
		scrollPane1 = new JScrollPane();
		scrollPane1.setViewportView(list);
		add(scrollPane1);
	}

	public String cardName() {
		return "Statistics";
	}
}
