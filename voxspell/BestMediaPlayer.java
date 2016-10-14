package voxspell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
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
	private EmbeddedMediaPlayer _video;

	/**
	 * Filters for the video.
	 * @author Ray Akau'ola
	 * @author Max McLaren
	 */
	public enum Filter {
		NORMAL, NEGATIVE
	}

	public static final String NORMAL_VIDEO = "reward.avi";
	public static final String NEGATIVE_VIDEO = "negative_reward.avi";

	private JButton play;
	private JButton stop;

	private JPanel screen;
	private JPanel controls;

	/**
	 * Sets up the video player GUI.
	 */
	public BestMediaPlayer(Filter filter) {

		JFrame frame = new JFrame("The Awesome Mediaplayer");

		mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

		final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();
		_video = video;

		screen = new JPanel();
		controls = new JPanel();

		screen.setLayout(new BorderLayout());
		screen.add(mediaPlayerComponent, BorderLayout.CENTER);

		// Button to play and pause the video.
		play = new JButton("PAUSE");
		controls.add(play);
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (video.isPlaying()) {
					video.pause();
					play.setText("PLAY");
				} else {
					video.play();
					play.setText("PAUSE");
				}
			}
		});

		// Button to stop the playing video.
		stop = new JButton("STOP");
		controls.add(stop);
		stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
				play.setText("PLAY");
			}
		});

		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setVisible(true);

		// Stop the video from playing when the window is closed
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cancel(true);
				_video.stop();
				mediaPlayerComponent.release();
			}
		});

		screen.add(controls, BorderLayout.SOUTH);
		frame.setContentPane(screen);

		// Play the video
		if (filter == Filter.NORMAL) {
			video.playMedia(NORMAL_VIDEO);
		} else {
			this.execute();
		}

	}

	/**
	 * Create a negative variant of the video
	 */
	@Override
	protected Void doInBackground() throws Exception {
		if (!negativeExists()) {
			String cmd = "ffmpeg -y -i "+NORMAL_VIDEO+" -vf negate "+NEGATIVE_VIDEO;
			ProcessBuilder pb = new ProcessBuilder("/bin/bash", "-c", cmd);
			try {
				Process process = pb.start();
				process.waitFor();
				if (isCancelled()) {
					process.destroy();
					return null;
				}
			} catch (Exception e) { }
		}
		return null;
	}

	/**
	 * Play the negative video.
	 */
	@Override
	protected void done() {
		if (!isCancelled()) {
			_video.playMedia(NEGATIVE_VIDEO);
		}
	}

	/**
	 * @return whether or not negative reward exists
	 */
	private boolean negativeExists() {
		File f = new File(NEGATIVE_VIDEO);
		if (f.exists() && !f.isDirectory()) {
			return true;
		} else {
			return false;
		}
	}

}
