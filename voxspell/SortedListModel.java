package voxspell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.AbstractListModel;

@SuppressWarnings("serial")
public class SortedListModel<E extends Comparable<? super E>> extends AbstractListModel<E> {

	List<E> list = new ArrayList<E>();
	
	public SortedListModel() {
		super();
	}

	@Override
	public E getElementAt(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public void clear() {
		list.clear();
	}
	
	public boolean addElement(E e) {
		return list.add(e);
	}
	
	public boolean contains(Object o) {
		return list.contains(o);
	}
	
	public void sort() {
		Collections.sort(list);
	}
}
