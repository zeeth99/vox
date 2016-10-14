package voxspell.quiz;

import java.io.File;
import javax.swing.DefaultListModel;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	/**
	 * 
	 * @param sp - The SpellingAid that created this
	 */
	public ReviewSelect(SpellingAid sp) {
		super(sp);
	}

	protected void setupListModel(DefaultListModel<WordList> listModel) {
		for (File f : SpellingAid.STATSFOLDER.listFiles())
			for (File f1 : f.listFiles())
				if (f1.getName().endsWith(".recent") && f1.length() > 0)
					listModel.addElement(new ReviewList(f1));
	}
}
