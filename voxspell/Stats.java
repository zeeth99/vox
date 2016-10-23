package voxspell;

import java.io.File;

import javax.swing.JList;
import javax.swing.JScrollPane;

import voxspell.quiz.StatsList;
import javax.swing.JSplitPane;

/**
 * A screen which shows statistics on the user's quizzes.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Stats extends Card {

	private JList<StatsList> list;
	private StatsPanel statsPanel;
	private JSplitPane splitPane;

	/**
	 * Sets up the GUI
	 * @param sp - The SpellingAid that created this
	 */
	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");

		// The panel with word statistics
		statsPanel = new StatsPanel();

		// List of all attempted categories
		list = new JList<StatsList>(new SortedListModel<StatsList>());
		list.setSelectionModel(new ToggleListSelectionModel(list));
		list.addListSelectionListener(statsPanel);
		setup();

		// Splits the screen with the category list on the left and statistics for selected categories on the right
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), statsPanel);
		splitPane.setBounds(15, 55, 465, 290);
		splitPane.setDividerLocation(120);
		add(splitPane);
	}

	/**
	 * Adds all attempted categories to the list and sorts it
	 */
	public void setup() {
		list.removeListSelectionListener(statsPanel);
		statsPanel.model.setRowCount(0);
		SortedListModel<StatsList> listModel = (SortedListModel<StatsList>)list.getModel();
		listModel.clear();
		addFolderToList(FileAccess.STATSFOLDER, listModel);
		listModel.sort();
		list.addListSelectionListener(statsPanel);
	}
	
	/**
	 * Add statistics from all files in the directory. Recursive.
	 * @param directory - The directory to look for stats files in
	 * @param listModel - The ListModel to add stats to
	 */
	private void addFolderToList(File directory, SortedListModel<StatsList> listModel) {
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(new StatsList(directory, s.substring(0, s.length()-6)));
			}
		}
	}

	public String cardName() {
		return "Statistics";
	}
}
