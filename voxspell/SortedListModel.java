package voxspell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class SortedListModel<E extends Comparable<? super E>> extends AbstractListModel<E> {

	private List<E> list;
	
	public SortedListModel() {
		super();
		list = new ArrayList<E>();
	}

	@Override
	public E getElementAt(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getSize() {
		return list.size();
	}
	
	public void clear() {
		fireIntervalRemoved(this, 0, getSize());
		list.clear();
	}
	
	public void addElement(E e) {
		list.add(e);
		fireIntervalAdded(this, getSize()-1, getSize()-1);
	}
	
	public boolean contains(Object o) {
		return list.contains(o);
	}
	
	public void sort() {
		Collections.sort(list);
		this.fireContentsChanged(this, 0, getSize());
	}
}
