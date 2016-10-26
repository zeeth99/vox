package voxspell.quiz;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.Card;
import voxspell.CardManager;
import voxspell.ErrorMessage;
import voxspell.FileAccess;
import voxspell.SortedListModel;

import javax.swing.JList;

/**
 * Screen to select a category to be tested on.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class CategorySelect extends Card implements ActionListener{

	protected JButton fileSelect;
	private JButton startQuiz;
	private JScrollPane scrollPane;
	protected JList<WordList> list;
	protected SortedListModel<WordList> listModel;

	/**
	 * Set up GUI
	 * @param cm - The CardManager that created this
	 */
	public CategorySelect(CardManager cm) {
		super(cm, "Select Your WordList");

		listModel = new SortedListModel<WordList>();
		setupListModel();
		list = new JList<WordList>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addKeyListener(new KeyAdapter(){ // Use enter to start a quiz
			public void keyPressed(KeyEvent e){
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					if (list.getSelectedValue() != null)
						cardManager.startQuiz(list.getSelectedValue());
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
		startQuiz.addActionListener(this);
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

	/**
	 * Add all categories in the word list directory to the list.
	 * @param listModel - ListModel to add categories to
	 */
	public void setupListModel() {
		for (File f : new File(FileAccess.WORDFOLDER).listFiles()) {
			try {
				Scanner sc = new Scanner(f);
				String str;
				// Check each line for a category.
				while (sc.hasNextLine()) {
					str = sc.nextLine();
					if (str.startsWith("%")) {
						if (listModel.contains(str.substring(1))) {
							String message = "The file, "+f+", contains multiple categories called "
									+str.substring(1)+"./nOnly the first will be used.";
							new ErrorMessage(message, "Category Naming conflict");
						} else {
							listModel.addElement(new WordList(f, str.substring(1)));
						}
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				new ErrorMessage(e);
				continue;
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == startQuiz) {
			if (list.getSelectedValue() != null)
				cardManager.startQuiz(list.getSelectedValue());
		} else if (e.getSource() == fileSelect) {
			FileAccess.addWordList();
			setupListModel();
		}
	}

	@Override
	public void onCardShown() {
		setupListModel();
	}
	
	public void onCardHidden() {
		listModel.clear();
	}
	
	public String cardName() {
		return "Category Select";
	}
}
