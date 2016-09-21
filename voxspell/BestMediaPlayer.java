package voxspell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingWorker;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class BestMediaPlayer extends SwingWorker<Void,Void> {
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private EmbeddedMediaPlayer _video;
	
	// Filters
	public static final int NORMAL = 0;
	public static final int NEGATIVE = 1;
	
	public static final String NORMAL_VIDEO = "big_buck_bunny_1_minute.avi";
	public static final String NEGATIVE_VIDEO = "negative_big_buck_bunny_1_minute.avi";
	
	private JButton _play;
	private JButton _pause;
	private JButton _stop;
	private JButton _exit;
	
	private JPanel _screen;
	private JPanel _controls;
	
	private BestMediaPlayer _this;
	
    public BestMediaPlayer(int filter) {
        JFrame frame = new JFrame("The Awesome Mediaplayer");
        _this = this;
        
        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();
        _video = video;
        
        _screen = new JPanel();
        _controls = new JPanel();
        
        _screen.setLayout(new BorderLayout());
        _screen.add(mediaPlayerComponent, BorderLayout.CENTER);
        
        _play = new JButton("PLAY");
        _controls.add(_play);
        _play.addActionListener(new ActionListener() {
        	@Override
        	public void actionPerformed(ActionEvent e) {
        		video.play();
        	}
        });
        
        _pause = new JButton("PAUSE");
        _controls.add(_pause);
        _pause.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.pause();
			}
		});
        
        _stop = new JButton("STOP");
        _controls.add(_stop);
        _stop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
			}
		});
        
        _exit = new JButton("EXIT");
        _controls.add(_exit);
        _exit.addActionListener(new ActionListener() {
        	@Override
			public void actionPerformed(ActionEvent e) {
				video.stop();
				_this.cancel(true);
				mediaPlayerComponent.release();
				frame.dispose();
			}
        });
        
        frame.setLocation(100, 100);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	_this.cancel(true);
            	_video.stop();
                mediaPlayerComponent.release();
            }
        });
        
        _screen.add(_controls, BorderLayout.SOUTH);
        frame.setContentPane(_screen);
        
        if (filter == NORMAL) {
        	video.playMedia(NORMAL_VIDEO);
        } else {
        	this.execute();
        }
        
    }

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
	
	@Override
	protected void done() {
		if (!isCancelled()) {
			_video.playMedia(NEGATIVE_VIDEO);
		}
	}
	
	private boolean negativeExists() {
		File f = new File(NEGATIVE_VIDEO);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	
}
