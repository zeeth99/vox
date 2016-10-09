package voxspell;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class StatsPanel extends JScrollPane implements ListSelectionListener {
	
	public StatsPanel(Stats stats) {
		super();
		JTable table = new JTable(new DefaultTableModel());
		table.setAutoCreateRowSorter(true);
		table.setEnabled(false);
		this.setViewportView(table);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub

		revalidate();
		repaint();
	}
}
