package voxspell.quiz;

import java.io.File;
import javax.swing.DefaultListModel;
import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	public ReviewSelect(SpellingAid sp) {
		super(sp);
	}

	protected void setupListModel(DefaultListModel<WordList> listModel) {
		for (File f : SpellingAid.STATSFOLDER.listFiles())
			if (f.getName().endsWith(".recent") && f.length() > 0)
				listModel.addElement(new ReviewList(f));
	}
}
