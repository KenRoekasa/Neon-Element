package client.audiomanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    private static double volume;
    private HashMap<Sound, MediaPlayer> soundLibray;

    public AudioManager(){
        volume = 0;
        soundLibray = new HashMap<>();

        for(Sound sound: Sound.values()){
            String location = sound.getPath();

            Media audioFile = new Media(new File(location).toURI().toString());
            //MediaPlayer mp = new MediaPlayer(audioFile);

           // soundLibray.put(sound, mp);
        }

    }

    public void playSound(Sound s){
        soundLibray.get(s).setVolume(volume);
        soundLibray.get(s).play();
        soundLibray.get(s).stop();
    }

    public double getVolume() {
        return volume;
    }

    public static void setVolume(double volume) {
        AudioManager.volume = volume;
    }

}
