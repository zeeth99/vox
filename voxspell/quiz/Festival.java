package voxspell.quiz;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingWorker;

/**
 * This class is used to call a festival text to speech command through Java.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
public class Festival extends SwingWorker<Void,Void> {

	public static final File SCHEME_FILE = new File(".festival/message.scm");

	private String _message;
	private Quiz _quiz;

	public Festival(String message, Quiz quiz) {
		_quiz = quiz;
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
		} catch (Exception e) { }
		return null;
	}

	/**
	 * Re-enable quiz buttons when done.
	 */
	@Override
	protected void done() {
		_quiz.setButtonsEnabled(true);
	}

	/**
	 * 
	 * @param message - A String representing the message to be spoken
	 */
	private void addMessageToScheme(String[] message) { 
		try {   
			List<String> linesToWrite = new ArrayList<>(); 
			List<String> lines = Files.readAllLines(Festival.SCHEME_FILE.toPath()); 
			linesToWrite.add(lines.get(0)); 
			for (String s : message) { 
				String messageToAdd = "(SayText \""+s+"\")"; 
				linesToWrite.add(messageToAdd); 
			} 
			Files.write(Festival.SCHEME_FILE.toPath(), linesToWrite); 
		} catch (Exception e) { } 
	} 
}