package voxspell.stats;

import java.io.File;

import javax.swing.JList;
import javax.swing.event.ListSelectionListener;

import voxspell.resource.FileAccess;
import voxspell.resource.SortedListModel;
import voxspell.resource.ToggleListSelectionModel;

@SuppressWarnings("serial")
public class StatsCategoryList extends JList<StatsCategory> {

	public StatsCategoryList() {
		super(new SortedListModel<StatsCategory>());
		setSelectionModel(new ToggleListSelectionModel(this));
		setup();
	}

	/**
	 * Adds all attempted categories to the list and sorts it
	 */
	public void setup() {
		ListSelectionListener[] listeners = getListSelectionListeners();
		for (ListSelectionListener l : listeners)
			removeListSelectionListener(l);
		
		SortedListModel<StatsCategory> listModel = (SortedListModel<StatsCategory>)getModel();
		listModel.clear();
		addFolderToList(new File(FileAccess.STATSFOLDER), listModel);
		
		for (ListSelectionListener l : listeners)
			addListSelectionListener(l);
	}

	/**
	 * Add statistics from all files in the directory. Recursive.
	 * @param directory - The directory to look for stats files in
	 * @param listModel - The ListModel to add stats to
	 */
	private void addFolderToList(File directory, SortedListModel<StatsCategory> listModel) {
		for (File f : directory.listFiles()) {
			if (f.isDirectory()) {
				addFolderToList(f, listModel);
			} else {
				String s = f.getName();
				if (s.endsWith(".stats") && f.length() > 0)
					listModel.addElement(new StatsCategory(directory, s.substring(0, s.length()-6)));
			}
		}
	}
}
