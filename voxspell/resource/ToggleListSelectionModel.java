package voxspell.resource;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JList;

/**
 * This ListSelectionModel allows the user to click on an element in the list to toggle selection
 * of that element without affecting the selection state of the other elements in the list. Also 
 * allows movement through the items in the list using arrow keys, then selection toggling with 
 * the space bar.
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
		
		_list.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				ToggleListSelectionModel.this.moveLeadSelectionIndex(0);
			}
		});

		for (MouseListener l : _list.getMouseListeners())
			_list.removeMouseListener(l);
	
		_list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				int index = _list.locationToIndex(e.getPoint());
				if (index >= 0 && e.getButton() == MouseEvent.BUTTON1)
					toggleSelection(index);
				e.consume();
			}
		});
		
		_list.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				int currentIndex = _list.getLeadSelectionIndex();
				int keyCode = e.getKeyCode();
				if (keyCode == KeyEvent.VK_SPACE || keyCode == KeyEvent.VK_ENTER) {
					// Toggle item on enter or space
					ToggleListSelectionModel.this.toggleSelection(currentIndex);
				} else if (keyCode == KeyEvent.VK_DOWN) {
					// Move down without changing selection state
					if (currentIndex < _list.getModel().getSize()-1)
						ToggleListSelectionModel.this.moveLeadSelectionIndex(currentIndex+1);
				} else if (keyCode == KeyEvent.VK_UP) {
					// Move up without changing selection state
					if (currentIndex > 0)
						ToggleListSelectionModel.this.moveLeadSelectionIndex(currentIndex-1);
				} else {
					return;
				}
				e.consume();
			}
		});
	};

	/**
	 * Toggle whether index is selected.
	 * @param index the index of the element to be toggled
	 */
	private void toggleSelection(int index) {
		if(!_list.isSelectedIndex(index))
			_list.addSelectionInterval(index, index);
		else
			_list.removeSelectionInterval(index, index);
	}

}
