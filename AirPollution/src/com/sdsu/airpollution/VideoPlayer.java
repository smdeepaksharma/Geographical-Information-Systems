package com.sdsu.airpollution;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sun.jna.NativeLibrary;

import uk.co.caprica.vlcj.component.EmbeddedMediaPlayerComponent;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;


class VideoPlayer extends JFrame {

    public VideoPlayer() {
         PlayerPanel player = new PlayerPanel();
         this.setTitle("Swing Video Player");
         this.setDefaultCloseOperation(EXIT_ON_CLOSE);
         this.setLayout(new BorderLayout());
         this.setSize(640, 480);
         this.setLocationRelativeTo(null);
         this.add(player, BorderLayout.CENTER);
         this.validate();
         this.setVisible(true);

         player.play("E:/CommonWorkSpace/GIS/AirPollution/video/pd.mp4");
    }

     public static void main(String[] args) {
         new VideoPlayer();
     }
}