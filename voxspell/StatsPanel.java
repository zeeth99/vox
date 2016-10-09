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

@SuppressWarnings("serial")
public class StatsPanel extends JScrollPane implements ListSelectionListener {

	DefaultTableModel model;
	JTable table;
	
	public StatsPanel() {
		super();
		String[] columnNames = {"Word", "Score", "Successes", "Attempts"};
		model = new DefaultTableModel(columnNames, 0);
		table = new JTable(model);
		table.setAutoCreateRowSorter(true);
		table.setEnabled(false);
		this.setViewportView(table);

	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		if (!e.getValueIsAdjusting()) {
			@SuppressWarnings("unchecked")
			JList<StatsList> list = (JList<StatsList>)e.getSource();
			for (int i = e.getFirstIndex(); i <= e.getLastIndex(); i++) {
				if (list.isSelectedIndex(i) && !isIncluded(i)) {
					StatsList sl = list.getModel().getElementAt(i);
					try {
						sl.setup();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ArrayList<String[]> arrayList = sl.statsInfo();
					for (String[] s : arrayList)
						model.addRow(s);
				} else if (!list.isSelectedIndex(i) && isIncluded(i)){
				}
			}
			revalidate();
			repaint();
		}
	}

	private boolean isIncluded(int i) {
		//TODO 
		return false;
	}
}
