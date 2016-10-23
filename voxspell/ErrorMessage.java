package voxspell;

import java.io.FileNotFoundException;

import javax.swing.JOptionPane;

/**
 * This class creates error message using the Swing JOptionPane class. 
 * @author Max McLaren
 */
public class ErrorMessage {
	
	/**
	 * Creates an error message with the given message and title.
	 * @param message - The message to be displayed in the error message
	 * @param title - The title of the error message
	 */
	public ErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Created an error message for a FileNotFoundException.
	 * @param e - the FileNotFoundException that caused this error message
	 */
	public ErrorMessage(FileNotFoundException e) {
		this("The file that was being access no longer exists", "File Not Found");
	}

	/**
	 * Creates an error message from an Exception. The error message uses the message of the exception.
	 * @param e - The Exception that caused this error message.
	 */
	public ErrorMessage(Exception e) {
		this(e.getMessage(), "An unknown error occurred");
	}

}
