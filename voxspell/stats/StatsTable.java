package voxspell.stats;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;

import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import voxspell.ErrorMessage;

/**
 * A panel which shows the user statistics on the words they have attempted
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class StatsTable extends JTable implements ListSelectionListener {

	DefaultTableModel model;
	ArrayList<StatsCategory> listOfDisplayedCategories;

	/**
	 * Set up the GUI
	 */
	public StatsTable() {
		super();
		String[] columnNames = {"Word", "Score", "Successes", "Attempts"};
		model = new DefaultTableModel(columnNames, 0);
		setModel(model);
		setAutoCreateRowSorter(true);
		setEnabled(false);
		setupSorting();
		listOfDisplayedCategories = new ArrayList<StatsCategory>();
	}

	private void setupSorting() {
		TableRowSorter<?> rowSorter = ((TableRowSorter<?>)getRowSorter());

		// Sort Score so that the highest score is at the top with default order.
		Comparator<String> scoreComparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.compare(o2.length(), o1.length());
			}
		};
		rowSorter.setComparator(1, scoreComparator);

		// Sort Successes and Attempts columns as integers.
		Comparator<String> stringIntegerComparator = new Comparator<String>() {
			@Override
			public int compare(String o1, String o2) {
				return Integer.compare(Integer.parseInt(o2), Integer.parseInt(o1));
			}
		};
		rowSorter.setComparator(2, stringIntegerComparator);
		rowSorter.setComparator(3, stringIntegerComparator);

	}

	/**
	 * Update the table when categories are selected or unselected from the list this listens to.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		if (!e.getValueIsAdjusting()) {
			@SuppressWarnings("unchecked")
			JList<StatsCategory> list = (JList<StatsCategory>)e.getSource();
			// For each category which might have changed selection status
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				StatsCategory sl;
				try {
					sl = list.getModel().getElementAt(i);
					sl.setup();
				} catch (FileNotFoundException e1) {
					new ErrorMessage(e1);
					continue;
				} catch (IndexOutOfBoundsException e1) {
					//Not enough time to debug...
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
