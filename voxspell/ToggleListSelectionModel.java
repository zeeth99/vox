package voxspell;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;

/**
 * This ListSelectionModel allows the user to click on an element in the list to select or
 * deselect it without affecting the selection state of the other elements in the list.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class ToggleListSelectionModel extends DefaultListSelectionModel {
	private JList<?> _list;

	/**
	 * @param list - the JList that this model is modelling
	 */
	public ToggleListSelectionModel(JList<?> list) {
		super();
		_list = list;
		_list.setFocusable(false);
		_list.setSelectionMode(SINGLE_SELECTION);
	};

	@Override
	public void setSelectionInterval(int i0, int i1) {
		// In single selection mode only i1 is used.
		if(!_list.isSelectedIndex(i1)) {
			_list.addSelectionInterval(i1, i1);
		} else {
			_list.removeSelectionInterval(i1, i1);
		}
	}
}
