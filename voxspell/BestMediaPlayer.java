package voxspell;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

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
	private EmbeddedMediaPlayer _video;
	private Filter _filter;

	/**
	 * Filters for the video.
	 * @author Ray Akau'ola
	 * @author Max McLaren
	 */
	public enum Filter {
		NORMAL, NEGATIVE
	}

	public static final String NORMAL_VIDEO = "media/reward.avi";
	public static final String NEGATIVE_VIDEO = "media/negative_reward.avi";

	private JButton play;
	private JButton stop;

	private JPanel screen;
	private JPanel controls;

	/**
	 * Sets up the video player GUI.
	 */
	@SuppressWarnings("serial")
	public BestMediaPlayer(Filter filter) {
		_filter = filter;

		JDialog frame = new JDialog() {{
			setModalityType(Dialog.ModalityType.APPLICATION_MODAL);
		}};

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


		// Stop the video from playing when the window is closed


		screen.add(controls, BorderLayout.SOUTH);
		frame.setContentPane(screen);
		frame.setLocation(100, 100);
		frame.setSize(1050, 600);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		execute();
		frame.setVisible(true);
		cancel(true);
		_video.stop();
		mediaPlayerComponent.release();
	}

	/**
	 * Create a negative variant of the video
	 */
	@Override
	protected Void doInBackground() throws Exception {
		if (_filter == Filter.NORMAL) {
			_video.playMedia(NORMAL_VIDEO);
		} else {
			if (!negativeExists()) {
				String cmd = "ffmpeg -y -i "+NORMAL_VIDEO+" -vf negate "+NEGATIVE_VIDEO;
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
			_video.playMedia(NEGATIVE_VIDEO);
		}
		return null;
	}

	/**
	 * Play the negative video.
	 */
	@Override
	protected void done() {
	}

	/**
	 * @return whether or not negative reward exists
	 */
	private boolean negativeExists() {
		File f = new File(NEGATIVE_VIDEO);
		if (f.exists() && !f.isDirectory())
			return true;
		return false;
	}

}
