package voxspell;

import java.io.FileNotFoundException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class ErrorMessage {
	
	public ErrorMessage(String message, String title) {
		JOptionPane.showMessageDialog(new JFrame(), message, title, JOptionPane.ERROR_MESSAGE);
	}

	public ErrorMessage(FileNotFoundException e) {
		this("The file that was being access no longer exists", "File Not Found");
	}

	public ErrorMessage(Exception e) {
		this(e.getMessage(), "An unknown error occurred");
	}

}
