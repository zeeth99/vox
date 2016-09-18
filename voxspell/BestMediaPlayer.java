package voxspell;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import uk.co.caprica.vlcj.binding.LibVlc;
import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.runtime.RuntimeUtil;

import com.sun.jna.Native;
import com.sun.jna.NativeLibrary;

public class BestMediaPlayer {
	private final EmbeddedMediaPlayerComponent mediaPlayerComponent;

    public BestMediaPlayer(String[] args) {
        JFrame frame = new JFrame("The Awesome Mediaplayer");

        mediaPlayerComponent = new EmbeddedMediaPlayerComponent();

        final EmbeddedMediaPlayer video = mediaPlayerComponent.getMediaPlayer();
        
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mediaPlayerComponent, BorderLayout.CENTER);
        
        frame.setContentPane(panel);

        JButton btnMute = new JButton("Shh....");
        panel.add(btnMute, BorderLayout.NORTH);
        btnMute.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.mute();
			}
		});
        
        JButton btnSkip = new JButton("Hurry up!");
        btnSkip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(5000);
			}
		});
        panel.add(btnSkip, BorderLayout.EAST);

        JButton btnSkipBack = new JButton("Say what!?!?");
        btnSkipBack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				video.skip(-5000);
			}
		});
        panel.add(btnSkipBack, BorderLayout.WEST);
        
        JLabel labelTime = new JLabel("0 seconds");
        panel.add(labelTime, BorderLayout.SOUTH);

        Timer timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long time = (long)(video.getTime()/1000.0);
				labelTime.setText(String.valueOf(time));
			}
		});
        timer.start();
        
        frame.setLocation(100, 100);
        frame.setSize(1050, 600);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
        
        String filename = "big_buck_bunny_1_minute.avi";
        video.playMedia(filename);
        
    }
}
