package audiomanager;

import java.io.File;
import java.util.ArrayList;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class GameMusic {
	
	private ArrayList<String> songs;
	
	public GameMusic(String files) {
		songs = new ArrayList<>();
		for(String file: files) 
			songs.add("/Users/KFM");
	}
	
	void play() {
		String bip = "bip.mp3";
		Media hit = new Media(new File(bip).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
	}

}
