package client.audiomanager;

import client.ClientGameState;
import engine.entities.Player;
import engine.enums.Action;
import javafx.scene.media.AudioClip;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    //todo Range of volume? In opitonController is set from 0 to 1.0;default value is 0.4
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
        System.out.println(volume);
    }

    public void playSound(Sound s, double volumeDistance) {
        effects.get(s).play(volumeDistance * volume);
    }

    public double getVolume() {
        return volume;
    }
    public void setVolume(double volume) {
        AudioManager.volume = volume;
    }


    // todo improve this
    public void clientLoop(ClientGameState gameState) {

        if(gameState.getPlayer().isAlive()) {
            if(gameState.getPlayer().getCurrentAction() != Action.IDLE && !gameState.getPlayer().hasActionSounded()) {


                playSound(Sound.switchSound(gameState.getPlayer().getCurrentAction()));
                gameState.getPlayer().setActionHasSounded(true);
            }


            for(Player enemy: gameState.getOtherPlayers(gameState.getPlayer())) {

                if(enemy.getCurrentAction() != Action.IDLE && !enemy.hasActionSounded()) {


                    double distance = gameState.getPlayer().getLocation().distance(enemy.getLocation());
                    double func = 1 / (distance);
                    func = func * 100;

                    // calculate distance

                    playSound(Sound.switchSound(enemy.getCurrentAction()), func);
                    enemy.setActionHasSounded(true);
                }

            }
        }


    }
}
