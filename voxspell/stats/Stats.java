package voxspell.stats;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import voxspell.Card;
import voxspell.SpellingAid;

/**
 * A screen which shows statistics on the user's quizzes.
 * @author Max McLaren
 */
@SuppressWarnings("serial")
public class Stats extends Card {

	private StatsCategoryList list;
	private StatsPanel statsPanel;
	private JSplitPane splitPane;

	/**
	 * Sets up the GUI
	 * @param sp - The SpellingAid that created this
	 */
	public Stats(SpellingAid sp) {
		super(sp, "Spelling Statistics");

		// The panel with word statistics
		statsPanel = new StatsPanel();

		// List of all attempted categories
		list = new StatsCategoryList();
		list.addListSelectionListener(statsPanel);
		setDefaultFocusComponent(list);

		// Splits the screen with the category list on the left and statistics for selected categories on the right
		splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(list), new JScrollPane(statsPanel));
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
