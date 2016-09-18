package voxspell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


public class BestMediaPlayer {
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;
	private EmbeddedMediaPlayer _video;

	private JButton _play;
	private JButton _pause;
	private JButton _stop;
	
	private JPanel _screen;
	private JPanel _controls;
	
    public BestMediaPlayer() {
        JFrame frame = new JFrame("The Awesome Mediaplayer");

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
        
        frame.setLocation(100, 100);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
            	_video.stop();
                mediaPlayerComponent.release();
            }
        });
        
        _screen.add(_controls, BorderLayout.SOUTH);
        frame.setContentPane(_screen);
        
        String filename = "big_buck_bunny_1_minute.avi";
        video.playMedia(filename);
        
    }
}
