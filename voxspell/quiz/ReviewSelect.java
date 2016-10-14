package voxspell.quiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import voxspell.FileAccess;
import voxspell.SortedListModel;
import voxspell.SpellingAid;
import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	private int _maximumScore;
	private ActionListener listener;

	/**
	 * Set up GUI.
	 * @param sp - The SpellingAid that created this
	 */
	public ReviewSelect(SpellingAid sp) {
		super(sp);
		fileSelect.setEnabled(false);
		fileSelect.setVisible(false);

		JToggleButton twoStar = new JToggleButton("2");
		twoStar.setBounds(12,320,50,30);

		JToggleButton threeStar = new JToggleButton("3");
		threeStar.setBounds(62,320,50,30);

		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton buttonPressed = (JToggleButton) e.getSource();
				twoStar.setSelected(false);
				threeStar.setSelected(false);
				buttonPressed.setSelected(true);
				_maximumScore = Integer.parseInt(buttonPressed.getText());
				setupListModel(listModel);
			}
		};

		twoStar.addActionListener(listener);
		threeStar.addActionListener(listener);

		add(twoStar);
		add(threeStar);
		twoStar.doClick();
	}

	protected void setupListModel(SortedListModel<WordList> listModel) {
		listModel.clear();
		for (File f : FileAccess.STATSFOLDER.listFiles())
			for (File f1 : f.listFiles())
				if (f1.getName().endsWith(".recent") && f1.length() > 0){
					ReviewList reviewList = new ReviewList(f1);
					reviewList.setup(_maximumScore);
					if (reviewList.size() > 0)
						listModel.addElement(reviewList);
				}
		listModel.sort();
	}
}
