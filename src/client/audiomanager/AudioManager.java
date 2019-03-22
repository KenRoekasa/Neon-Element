package client.audiomanager;

import client.ClientGameState;
import engine.entities.Player;
import engine.enums.Action;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    private static double effectVolume;

    private HashMap<Sound, AudioClip> gameEffects;
    private HashMap<Music, Media> gameMusic;
    private MediaPlayer musicMediaPlayer;
    private MediaPlayer fxMediaPlayer;
    public AudioManager(){
        effectVolume = 100;
        gameEffects = new HashMap<>();
        gameMusic = new HashMap<>();

        for(Sound sound: Sound.values()){
            String location = sound.getPath();

            AudioClip audioClip = new AudioClip(new File(location).toURI().toString());

            gameEffects.put(sound, audioClip);
        }

        for(Music music: Music.values()) {
            String location = music.getPath();
//            URL resource = getClass().getResource(location);
            Media media = new Media(new File(location).toURI().toString());

            gameMusic.put(music, media);

        }

        musicMediaPlayer = new MediaPlayer(gameMusic.get(Music.MENU));
        // set mediaplayer to loop at the end of the music
        startMusic(musicMediaPlayer);

        fxMediaPlayer = new MediaPlayer(gameMusic.get(Music.NEON));
        startMusic(fxMediaPlayer);



    }

    public void playSound(Sound s){
        gameEffects.get(s).play(effectVolume);
    }

    private void    playSound(Sound s, double volumeDistance) {
        double sound_volume = volumeDistance * effectVolume;
        gameEffects.get(s).play(sound_volume);
    }

    public void setEffectVolume(double volume) {
        AudioManager.effectVolume = volume;
        fxMediaPlayer.setVolume(volume);
    }

    public void setNeonVolume(double volume) {
        fxMediaPlayer.setVolume(volume);
    }

    public double getEffectVolume() {
        return effectVolume;
    }



    public void setMusicVolume(double volume) {
        musicMediaPlayer.setVolume(volume);
    }


    public void setGameMusic(Music music){
        musicMediaPlayer.setOnEndOfMedia(() -> {
            musicMediaPlayer = new MediaPlayer(gameMusic.get(music));
            startMusic(musicMediaPlayer);
        });

    }

    private void startMusic(MediaPlayer mediaPlayer){
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
            }
        });
        mediaPlayer.play();
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
                    double func = 1 / (distance) * 100;


                    System.out.println(func);

                    // calculate distance

                    playSound(Sound.switchSound(enemy.getCurrentAction()), func);
                    enemy.setActionHasSounded(true);
                }

            }
        }


    }
}
