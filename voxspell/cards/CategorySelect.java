package voxspell.cards;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import voxspell.SpellingAid;
import voxspell.WordList;

import javax.swing.JList;

@SuppressWarnings("serial")
public class CategorySelect extends Card implements ActionListener{

	private JButton startQuiz;
	private JScrollPane scrollPane;
	private JList<WordList> list;

	public CategorySelect(SpellingAid sp) {
		super(sp, "Select Your WordList");

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 55, 488, 233);
		add(scrollPane);

		DefaultListModel<WordList> listModel = new DefaultListModel<WordList>();
		list = new JList<WordList>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		setupListModel(listModel);
		scrollPane.setViewportView(list);


		startQuiz = new JButton("Start Quiz");
		startQuiz.setBounds(383, 18, 117, 25);
		startQuiz.addActionListener(this);
		add(startQuiz);

	}

	protected void setupListModel(DefaultListModel<WordList> listModel) {
		for (File f : SpellingAid.WORDFOLDER.listFiles()) {
			try {
				Scanner sc = new Scanner(f);
				String str;
				while (sc.hasNextLine()) {
					str = sc.nextLine();
					if (str.startsWith("%"))
						listModel.addElement(new WordList(f, str.substring(1)));
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
