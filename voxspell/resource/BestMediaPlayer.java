package voxspell.resource;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;

/**
 * Creates and displays a video reward.
 * @author Ray Akau'ola
 * @author Max McLaren
 */
public class BestMediaPlayer extends SwingWorker<Void,Void> {
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	final private EmbeddedMediaPlayer _video;
	private Video _filter;

	/**
	 * Filters for the video.
	 * @author Ray Akau'ola
	 * @author Max McLaren
	 */
	public enum Video {
		NORMAL(FileAccess.VIDEOFOLDER+"/reward.avi"), NEGATIVE(FileAccess.VIDEOFOLDER+"/negative_reward.avi");
		String _file;

		Video(String file) {
			_file = file;
		}

		public String toString() {
			return _file;
		}
	}

	private JButton play;
	private JButton stop;

	private JPanel screen;
	private JPanel controls;

	/**
	 * Sets up the video player GUI.
	 */
	@SuppressWarnings("serial")
	public BestMediaPlayer(Video filter) {
		// Stop messages from VLCJ.
		PrintStream err = System.err;
		System.setErr(new PrintStream(new OutputStream() {
			@Override
			public void write(int b) throws IOException {
			}
		}));
		_filter = filter;

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();
		_video = mediaPlayerComponent.getMediaPlayer();

		screen = new JPanel();
		screen.setLayout(new BorderLayout());
		screen.add(mediaPlayerComponent, BorderLayout.CENTER);

		// Button to play and pause the video.
		play = new JButton("PAUSE");
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (_video.isPlaying()) {
					_video.pause();
					play.setText("PLAY");
				} else {
					_video.play();
					play.setText("PAUSE");
				}
			}
		});

		// Button to stop the playing video.
		stop = new JButton("STOP");
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				_video.stop();
				play.setText("PLAY");
			}
		});

		JDialog frame = new JDialog() {{
			setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		}};

		controls = new JPanel();
		controls.add(play);
		controls.add(stop);
		screen.add(controls, BorderLayout.SOUTH);

		frame.setContentPane(screen);
		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		execute();
		frame.setVisible(true);
		// The following code is executed once the window is closed.
		// It stops the video from playing.
		cancel(true);
		_video.stop();
		mediaPlayerComponent.release();
		System.setErr(err);
	}

	/**
	 * Decides which variant to display, then plays it.
	 * Create a negative variant of the video if it doesn't exist.
	 */
	@Override
	protected Void doInBackground() throws Exception {
		if (!negativeExists() && _filter == Video.NEGATIVE) {
			String cmd = "ffmpeg -y -i "+Video.NORMAL+" -vf negate "+Video.NEGATIVE;
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = pb.start();
				process.waitFor();
				if (isCancelled())
					process.destroy();
			} catch (IOException e) {
				new ErrorMessage(e);
			}
		}
		_video.playMedia(_filter.toString());
		return null;
	}

	/**
	 * @return whether or not negative reward exists
	 */
	private boolean negativeExists() {
		File f = new File(Video.NEGATIVE.toString());
		if (f.exists() && !f.isDirectory())
			return true;
		return false;
	}

}
