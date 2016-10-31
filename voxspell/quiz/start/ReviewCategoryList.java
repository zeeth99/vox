package voxspell.quiz.start;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JToggleButton;

import voxspell.quiz.ReviewList;
import voxspell.quiz.WordList;
import voxspell.resource.FileAccess;
import voxspell.resource.SortedListModel;

@SuppressWarnings("serial")
public class ReviewCategoryList extends RegularCategoryList implements ActionListener {

	private int _maximumScore;
	
	@Override
	public void setupListModel() {
		for (File f : new File(FileAccess.STATSFOLDER).listFiles())
			for (File f1 : f.listFiles())
				if (f1.getName().endsWith(".recent") && f1.length() > 0){
					ReviewList reviewList = new ReviewList(f1);
					reviewList.setup(_maximumScore);
					if (reviewList.size() > 0)
						((SortedListModel<WordList>)getModel()).addElement(reviewList);
				}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		JToggleButton buttonPressed = (JToggleButton) e.getSource();
		_maximumScore = Integer.parseInt(buttonPressed.getName());
		((SortedListModel<WordList>)getModel()).clear();
		setupListModel();
	}
}
