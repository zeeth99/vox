package voxspell.quiz;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.Card;
import voxspell.SpellingAid;

import javax.swing.JList;
import javax.swing.JOptionPane;

/**
 * Screen to select a category to be tested on.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class CategorySelect extends Card implements ActionListener{

	private JButton startQuiz;
	private JScrollPane scrollPane;
	private JList<WordList> list;

	/**
	 * Set up GUI
	 * @param sp - The SpellingAid that created this
	 */
	public CategorySelect(SpellingAid sp) {
		super(sp, "Select Your WordList");

		DefaultListModel<WordList> listModel = new DefaultListModel<WordList>();
		setupListModel(listModel);
		list = new JList<WordList>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		
		// List of categories
		scrollPane = new JScrollPane(list);
		scrollPane.setBounds(12, 55, 476, 233);
		add(scrollPane);

		// Button to start quiz with currently selected category
		startQuiz = new JButton("Start Quiz");
		startQuiz.setBounds(383, 18, 105, 25);
		startQuiz.addActionListener(this);
		add(startQuiz);
	}

	/**
	 * Add all categories in the word list directory to the list.
	 * @param listModel - ListModel to add categories to
	 */
	protected void setupListModel(DefaultListModel<WordList> listModel) {
		for (File f : SpellingAid.WORDFOLDER.listFiles()) {
			try {
				Scanner sc = new Scanner(f);
				String str;
				while (sc.hasNextLine()) {
					str = sc.nextLine();
					if (str.startsWith("%")) {
						if (listModel.contains(str.substring(1))) {
							JOptionPane.showMessageDialog(new JFrame(), "The file, "+f+", contains multiple categories with same name./nOnly the first category with the name: "+str.substring(1)+"will be useable.", "Category Naming Conflict", JOptionPane.ERROR_MESSAGE);
						} else {
							listModel.addElement(new WordList(f, str.substring(1)));
						}
					}
				}
				sc.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() == startQuiz)
			spellingAid.startQuiz(list.getSelectedValue());
	}

	public String cardName() {
		return "Category Select";
	}
}
