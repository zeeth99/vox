package voxspell.quiz;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

import voxspell.resource.ErrorMessage;

/**
 * This class is used to call a festival text to speech command through Java.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
public class Festival extends SwingWorker<Void,Void> {

	public static final File SCHEME_FILE = new File(".festival/message.scm");

	private String _message;
	private Quizzer _renameThis;

	public Festival(String message, Quizzer quizzer) {
		_renameThis = quizzer;
		_message = message;
	}

	/**
	 * Process and speak the message
	 */
	@Override
	protected Void doInBackground() throws Exception {
		String[] messageParts = _message.split(":");
		addMessageToScheme(messageParts);
		String cmd = "festival -b "+ SCHEME_FILE;
		ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
		try {
			Process process = pb.start();
			process.waitFor();
		} catch (IOException e) {
			new ErrorMessage(e);
		}
		return null;
	}

	/**
	 * Re-enable quiz buttons when done.
	 */
	@Override
	protected void done() {
		_renameThis.enableButtons();
	}

	/**
	 * Adds the provided message parts to the scheme file.
	 * The scheme file contains the words to speak as well as configuration for Festival.
	 * @param message - A String representing the message to be spoken
	 */
	private void addMessageToScheme(String[] message) {
		try {
			List<String> linesToWrite = new ArrayList<>();
			String firstLine = Files.readAllLines(SCHEME_FILE.toPath()).get(0);
			linesToWrite.add(firstLine);
			for (String s : message)
				linesToWrite.add("(SayText \""+s+"\")");
			Files.write(SCHEME_FILE.toPath(), linesToWrite);
		} catch (IOException e) {
			new ErrorMessage(e);
		}
	}
}
