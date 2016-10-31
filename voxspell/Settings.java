package voxspell;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import voxspell.quiz.Festival;
import voxspell.resource.ErrorMessage;
import voxspell.resource.FileAccess;

/**
 * Screen for changing the program's settings
 * @author Ray Akau'ola
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Settings extends Card implements ActionListener {

	public static final String DEFAULT_VOICE = "(voice_kal_diphone)";
	public static final String NZ_VOICE = "(voice_akl_nz_jdt_diphone)";

	private JButton clearStatistics;
	private JComboBox<String> voiceSettings;
	private JLabel voiceSettingsLabel;
	private JButton addNewWord;

	/**
	 * Set up the GUI
	 * @param cm - The CardManager that created this
	 */
	public Settings(CardManager cm) {
		super(cm, "Settings");

		voiceSettingsLabel = new JLabel("Select a voice setting:");
		voiceSettingsLabel.setBounds(100, 80, 300, 20);

		ImageIcon voiceSettingsIcon = new ImageIcon(FileAccess.getMedia("Microphone.png"));
		voiceSettingsLabel.setIcon(voiceSettingsIcon);

		// Drop down menu to select the voice for Festival to use
		voiceSettings = new JComboBox<String>();
		voiceSettings.addItem("Default");
		voiceSettings.addItem("New Zealand");
		voiceSettings.setBounds(100, 100, 300, 20);
		voiceSettings.setToolTipText("Change the voice VOXSPELL uses in quizzes.");
		voiceSettings.setSelectedIndex(0);
		voiceSettings.addActionListener(new ActionListener()	{
			public void actionPerformed(ActionEvent e){
				@SuppressWarnings("unchecked")
				JComboBox<String> combo = (JComboBox<String>)e.getSource();
				String voice = (String)combo.getSelectedItem();
				switch (voice) {
				case "Default":
					changeVoiceSetting(DEFAULT_VOICE);
					break;
				case "New Zealand":
					changeVoiceSetting(NZ_VOICE);
					break;
				}
			}
		});

		// Button to delete all history files
		clearStatistics = new JButton("Clear Statistics");
		clearStatistics.setFont(new Font("Dialog", Font.BOLD, 16));
		clearStatistics.setBounds(100, 270, 300, 50);
		clearStatistics.setToolTipText("Delete all spelling statistics.");
		clearStatistics.addActionListener(this);

		ImageIcon clearStatisticsIcon = new ImageIcon(FileAccess.getMedia("Delete_2x.png"));
		clearStatistics.setLayout(new BorderLayout());
		clearStatistics.add(new JLabel(clearStatisticsIcon), BorderLayout.WEST);

		// Button to add a new word list
		addNewWord = new JButton("Add New Word List");
		addNewWord.setFont(new Font("Dialog", Font.BOLD, 16));
		addNewWord.setBounds(100, 210, 300, 50);
		addNewWord.setToolTipText("Import an external word list.");
		addNewWord.addActionListener(this);

		ImageIcon addNewWordIcon = new ImageIcon(FileAccess.getMedia("Add_List_2x.png"));
		addNewWord.setLayout(new BorderLayout());
		addNewWord.add(new JLabel(addNewWordIcon), BorderLayout.WEST);

		add(clearStatistics);
		add(voiceSettings);
		add(voiceSettingsLabel);
		add(addNewWord);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		if (e.getSource() == clearStatistics)
			clearStats();
		if (e.getSource() == addNewWord)
			FileAccess.addWordList();
	}

	/**
	 * Changes the voice to use in quizzes to setting.
	 * @param setting - The voice to be used
	 */
	private void changeVoiceSetting(String setting) {
		try {
			List<String> lines = Files.readAllLines(Festival.SCHEME_FILE.toPath());
			lines.set(0, setting);
			Files.write(Festival.SCHEME_FILE.toPath(), lines);
		} catch (IOException e) {
			new ErrorMessage(e);
		}
	}

	/**
	 * Opens a dialog to confirm. If confirmation is met, deletes all files in .history.
	 */
	public static void clearStats() {
		JFrame popupFrame = new JFrame();
		String message = "This will permanently delete all of your spelling history.\n"
				+ "Are you sure you want to do this?";
		int option = JOptionPane.showConfirmDialog(popupFrame, message, "Delete Statistics", JOptionPane.YES_NO_OPTION);
		if (option == JOptionPane.YES_OPTION)
			FileAccess.clearStats();
	}

	public String cardName() {
		return "Settings";
	}
}
