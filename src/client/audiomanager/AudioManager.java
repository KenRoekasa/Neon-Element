package client.audiomanager;

import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    private static double volume;
    private HashMap<Sound, AudioClip> effects;

    public AudioManager(){
        volume = 100;
        effects = new HashMap<>();

        for(Sound sound: Sound.values()){
            String location = sound.getPath();

            System.out.println(location);
            AudioClip audioClip = new AudioClip(new File(location).toURI().toString());

            effects.put(sound, audioClip);
        }

    }

    public void playSound(Sound s){
        effects.get(s).play(volume);

    }

    public double getVolume() {
        return volume;
    }
    public static void setVolume(double volume) {
        AudioManager.volume = volume;
    }

}
