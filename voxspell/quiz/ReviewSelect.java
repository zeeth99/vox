package voxspell.quiz;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.DefaultListModel;

import voxspell.SpellingAid;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	public ReviewSelect(SpellingAid sp) {
		super(sp);
	}


	protected void setupListModel(DefaultListModel<WordList> listModel) {
		String suffix = ".review";
		for (File f : SpellingAid.STATSFOLDER.listFiles()) {
			String str = f.getName();
			if (str.endsWith(suffix) && f.length() > 0) {
				String strTrim = str.substring(0, str.length()-suffix.length());
				listModel.addElement(new ReviewList(strTrim));
			}
		}
	}

	public void actionPerformed(ActionEvent e) {
		
		super.actionPerformed(e);
	}

}
