package voxspell.quiz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import voxspell.Card;
import voxspell.CardManager;
import voxspell.FileAccess;
import voxspell.SortedListModel;

/**
 * Screen to select a category to be tested on.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class CategorySelect extends Card {

	protected JButton fileSelect;
	public JButton startQuiz;
	protected JScrollPane scrollPane;
	protected CategoryList list;
	
	protected Quizzer _renameThis;

	/**
	 * Set up GUI
	 * @param cm - The CardManager that created this
	 */
	public CategorySelect(CardManager cm, QuizCard quiz) {
		super(cm, "Select Your WordList");
		_renameThis = quiz.quizzer;

		createList();
		list.addKeyListener(new KeyAdapter(){ // Use enter to start a quiz
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					startQuiz.doClick();
			}
		});
		setDefaultFocusComponent(list);

		// List of categories
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(12, 55, 476, 265);
		add(scrollPane);

		// Button to start quiz with currently selected category
		startQuiz = new JButton("Start Quiz");
		startQuiz.setBounds(383, 15, 105, 30);
		startQuiz.setToolTipText("Start a quiz with the selected category.");
		startQuiz.addActionListener(getCardManager());
		startQuiz.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_renameThis.setWordlist(list.getSelectedValue());
			}
		});
		startQuiz.setFocusable(false);
		add(startQuiz);

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
		list = new CategoryList();
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
	public void onCardShown() {
		list.setupListModel();
	}

	public void onCardHidden() {
		((SortedListModel<?>) list.getModel()).clear();
	}

	public String cardName() {
		return "Category Select";
	}
}
