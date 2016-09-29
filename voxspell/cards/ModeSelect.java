package voxspell.cards;

import java.awt.Component;
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
import javax.swing.JList;

@SuppressWarnings("serial")
public class ModeSelect extends Card implements ActionListener{

	private JButton startQuiz;
	private JScrollPane scrollPane;
	private JList<Category> list;

	public ModeSelect(SpellingAid sp) {
		super(sp, "Select Your Category");
				
		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 55, 488, 233);
		add(scrollPane);
		
		DefaultListModel<Category> listModel = new DefaultListModel<Category>();
		list = new JList<Category>(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
				
		for (File f : SpellingAid.WORDFOLDER.listFiles()) {
			try {
				Scanner sc = new Scanner(f);
			String str;
			while (sc.hasNextLine()) {
				str = sc.nextLine();
				if (str.startsWith("%")) {
					listModel.addElement(new Category(f, str.substring(1)));
				}
			}
			sc.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		scrollPane.setViewportView(list);
		
		
		startQuiz = new JButton("Start Quiz");
		startQuiz.setBounds(383, 18, 117, 25);
		startQuiz.addActionListener(this);
		add(startQuiz);

	}


	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if(e.getSource() == startQuiz) {
			Category cat = list.getSelectedValue();
			spellingAid.startQuiz(cat.getFile(), cat.toString());
		}
	}
}

@SuppressWarnings("serial")
class Category extends Component{
	File _file;
	String _name;
	
	Category(File f, String category) {
		_file = f;
		_name = category;
	}
	
	File getFile() {
		return _file;
	}

	public String toString() {
		return _name;
	}
}
