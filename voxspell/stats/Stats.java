package voxspell.stats;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import voxspell.Card;
import voxspell.CardManager;

/**
 * A screen which shows statistics on the user's quizzes.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Stats extends Card {

	private StatsCategoryList list;
	private StatsTable statsTable;
	private JSplitPane splitPane;

	/**
	 * Sets up the GUI
	 * @param cm - The CardManager that created this
	 */
	public Stats(CardManager cm) {
		super(cm, "Spelling Statistics");

		// The panel with word statistics
		statsTable = new StatsTable();

		// List of all attempted categories
		list = new StatsCategoryList();
		list.addListSelectionListener(statsTable);
		setDefaultFocusComponent(list);

		// Splits the screen with the category list on the left and statistics for selected categories on the right
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), new JScrollPane(statsTable));
		splitPane.setBounds(15, 55, 465, 290);
		splitPane.setDividerLocation(120);
		add(splitPane);
	}
	
	protected void onCardShown() {
		list.setup();
	}
	
	protected void onCardHidden() {
		list.clearSelection();
	}

	public String cardName() {
		return "Statistics";
	}
}
