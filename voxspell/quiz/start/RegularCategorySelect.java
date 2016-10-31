package voxspell.quiz.start;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import voxspell.CardManager;
import voxspell.quiz.QuizCard;
import voxspell.resource.FileAccess;

/**
 * Screen to select a category to be tested on.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class RegularCategorySelect extends AbstractCategorySelect {

	private JButton fileSelect;
	
	/**
	 * Set up GUI
	 * @param cm - The CardManager that created this
	 */
	public RegularCategorySelect(CardManager cm, QuizCard quiz) {
		super(cm, quiz);

		// Button to add an external file to the list of wordlists.
		fileSelect = new JButton("Select Other File");
		fileSelect.setBounds(12, 320, 200, 30);
		fileSelect.setToolTipText("Import an external word list.");
		fileSelect.addActionListener(this);
		fileSelect.setFocusable(false);
		add(fileSelect);

		ImageIcon fileSelectIcon = new ImageIcon(FileAccess.getMedia("Add_List.png"));
		fileSelect.setLayout(new BorderLayout());
		fileSelect.add(new JLabel(fileSelectIcon), BorderLayout.WEST);
	}

	protected void createList() {
		list = new RegularCategoryList();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == fileSelect) {
			FileAccess.addWordList();
			list.setupListModel();
		}
	}

	
	@Override
	protected void setReviewMode() {
		// TODO Auto-generated method stub
		_quiz.setReviewMode(false);
	}

}
