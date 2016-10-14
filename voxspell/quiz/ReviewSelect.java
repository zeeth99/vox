package voxspell.quiz;

import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.DefaultListModel;

import voxspell.FileAccess;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	/**
	 * Set up GUI.
	 * @param sp - The SpellingAid that created this
	 */
	public ReviewSelect(SpellingAid sp) {
		super(sp);
		fileSelect.setEnabled(false);
		fileSelect.setVisible(false);
	}

	protected void setupListModel(DefaultListModel<WordList> listModel) {
		for (File f : FileAccess.STATSFOLDER.listFiles())
			for (File f1 : f.listFiles())
				if (f1.getName().endsWith(".recent") && f1.length() > 0){
					ReviewList list = new ReviewList(f1);
					list.setup();
					if (list.size() > 0)
						listModel.addElement(list);
				}
	}
}
