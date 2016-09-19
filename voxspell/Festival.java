package voxspell;

import javax.swing.SwingWorker;

import voxspell.cards.Quiz;


public class Festival extends SwingWorker<Void,Void> {
	
	private String _message;
	private Quiz _quiz;
	
	public Festival(String message, Quiz quiz) {
		_quiz = quiz;
		_message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo \"" + _message + "\" | festival --tts");
		try {

			Process process = pb.start();
			process.waitFor();
			
		} catch (Exception e) { }
		return null;
	}
	
	@Override
	protected void done() {
		_quiz.enableButtons();
	}
}