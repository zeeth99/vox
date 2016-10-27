package voxspell.quiz; 
 
import java.io.File; 
import java.io.FileNotFoundException; 
import java.util.Scanner; 
 
import javax.swing.JList; 
import javax.swing.ListSelectionModel; 
 
import voxspell.ErrorMessage; 
import voxspell.FileAccess; 
import voxspell.SortedListModel; 
 
@SuppressWarnings("serial") 
public class CategoryList extends JList<WordList> { 
   
  public CategoryList() { 
    super(new SortedListModel<WordList>()); 
    setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
    setupListModel(); 
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
            SortedListModel<WordList> listModel = (SortedListModel<WordList>) getModel(); 
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
 
} 