package voxspell;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import voxspell.quiz.StatsList;

/**
 * A panel which shows the user statistics on the words they have attempted
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class StatsPanel extends JScrollPane implements ListSelectionListener {

	DefaultTableModel model;
	JTable table;
	ArrayList<StatsList> listOfDisplayedCategories;

	/**
	 * Set up the GUI
	 */
	public StatsPanel() {
		super();
		String[] columnNames = {"Word", "Score", "Successes", "Attempts"};
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.setEnabled(false);
		this.setViewportView(table);
		listOfDisplayedCategories = new ArrayList<StatsList>();
	}

	/**
	 * Update the table when categories are selected or unselected from the list this listens to.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			@SuppressWarnings("unchecked")
			JList<StatsList> list = (JList<StatsList>)e.getSource();
			// For each category which might have changed selection status
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				StatsList sl = list.getModel().getElementAt(i);
				try {
					sl.setup();
				} catch (FileNotFoundException e1) {
					new ErrorMessage(e1);
					continue;
				}
				
				// If category is selected and wasn't previously
				// Add all words from this category
				if (list.isSelectedIndex(i) && !listOfDisplayedCategories.contains(sl)) {
					listOfDisplayedCategories.add(sl);
					for (String[] s : sl.statsInfo())
						model.addRow(s);
				}
				
				// If category isn't selected and was previously
				// Remove all rows for words in this category
				if (!list.isSelectedIndex(i) && listOfDisplayedCategories.contains(sl)) {
					listOfDisplayedCategories.remove(sl);
					for (String word : sl.wordList())
						for (int row = 0; row < model.getRowCount(); row++) 
							if (word.equals(model.getValueAt(row, 0)))
								model.removeRow(row);
				}
			}
			// Visually update the table
			revalidate();
			repaint();
		}
	}

}
