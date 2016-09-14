package voxspell;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.swing.SwingWorker;

public class Festival extends SwingWorker<Void,Void> {
	
	private String _message;
	
	public Festival(String message) {
		_message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		ProcessBuilder pb = new ProcessBuilder("bash", "-c", "echo \"" + _message + "\" | festival --tts");
		try {

			Process process = pb.start();

			BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

			int exitStatus = process.waitFor();

			if (exitStatus == 0) {
				String line;
				while ((line = stdout.readLine()) != null) {
					System.out.println(line);
				}
			} else {
				String line;
				while ((line = stderr.readLine()) != null) {
					System.err.println(line);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
