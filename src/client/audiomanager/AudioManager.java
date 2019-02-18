package client.audiomanager;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    private double volume;
    private HashMap<Sound, MediaPlayer> soundLibray;

    public AudioManager(){
        volume = 100;
        soundLibray = new HashMap<>();

        for(Sound sound: Sound.values()){
            String location = "audio/" + sound.toString() + ".mp3";
            System.out.println(location);

            Media audioFile = new Media(new File(location).toURI().toString());
            MediaPlayer mp = new MediaPlayer(audioFile);

            soundLibray.put(sound, mp);
        }

    }

    public void playSound(Sound s){
        soundLibray.get(s).setVolume(volume);
        soundLibray.get(s).play();
        soundLibray.get(s).getTotalDuration();

        (new Thread(() -> {
            try {
                stopSound(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        })).start();
    }

    private void stopSound(Sound s) throws InterruptedException {
        wait((long) soundLibray.get(s).getTotalDuration().toMillis());
        soundLibray.get(s).stop();
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }


}
