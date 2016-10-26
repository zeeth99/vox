package voxspell.quiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import voxspell.CardManager;
import voxspell.FileAccess;

import javax.swing.JToggleButton;

@SuppressWarnings("serial")
public class ReviewSelect extends CategorySelect {

	private int _maximumScore;
	private ActionListener listener;

	/**
	 * Set up GUI.
	 * @param cm - The CardManager that created this
	 */
	public ReviewSelect(CardManager cm) {
		super(cm);
		fileSelect.setEnabled(false);
		fileSelect.setVisible(false);

		// following buttons dictate the number of stars a word can have to be considered reviewable.
		JToggleButton twoStar = new JToggleButton("Least known words");
		twoStar.setName("2");
		twoStar.setBounds(12,320,238,30);
		twoStar.setToolTipText("Change review mode word criterion.");
		twoStar.setFocusable(false);

		JToggleButton threeStar = new JToggleButton("All words without three stars");
		threeStar.setName("3");
		threeStar.setBounds(250,320,238,30);
		threeStar.setToolTipText("Change review mode word criterion.");
		threeStar.setFocusable(false);

		listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JToggleButton buttonPressed = (JToggleButton) e.getSource();
				twoStar.setSelected(false);
				threeStar.setSelected(false);
				buttonPressed.setSelected(true);
				_maximumScore = Integer.parseInt(buttonPressed.getName());
				setupListModel();
			}
		};

		twoStar.addActionListener(listener);
		threeStar.addActionListener(listener);

		add(twoStar);
		add(threeStar);
		twoStar.doClick();
	}

	@Override
	public void setupListModel() {
		listModel.clear();
		for (File f : new File(FileAccess.STATSFOLDER).listFiles())
			for (File f1 : f.listFiles())
				if (f1.getName().endsWith(".recent") && f1.length() > 0){
					ReviewList reviewList = new ReviewList(f1);
					reviewList.setup(_maximumScore);
					if (reviewList.size() > 0)
						listModel.addElement(reviewList);
				}
	}
}
