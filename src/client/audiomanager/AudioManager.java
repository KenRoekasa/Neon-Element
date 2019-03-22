package client.audiomanager;

import client.ClientGameState;
import engine.entities.Player;
import engine.enums.Action;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;

public class AudioManager {
    private static double effectVolume;
    private static double musicVolume;
    private HashMap<Sound, AudioClip> gameEffects;
    private MediaPlayer menuMusicMediaPlayer;
    private MediaPlayer gameMusicMediaPlayer;
    private MediaPlayer fxMediaPlayer;
    public AudioManager(){
        effectVolume = 100;
        musicVolume = 100;
        gameEffects = new HashMap<>();

        for(Sound sound: Sound.values()){
            String location = sound.getPath();

            AudioClip audioClip = new AudioClip(new File(location).toURI().toString());

            gameEffects.put(sound, audioClip);
        }


        initialiseGameMusic();



    }

    private void initialiseGameMusic() {
        String menuMusicLocation = Music.MENU.getPath();
        Media menuMedia = new Media(new File(menuMusicLocation).toURI().toString());
        menuMusicMediaPlayer = new MediaPlayer(menuMedia);
        // set to loop at end of track
        menuMusicMediaPlayer.setOnEndOfMedia(() -> menuMusicMediaPlayer.seek(Duration.ZERO));



        String gameMusicLocation = Music.GAME.getPath();
        Media gameMedia = new Media(new File(gameMusicLocation).toURI().toString());
        gameMusicMediaPlayer = new MediaPlayer(gameMedia);
        gameMusicMediaPlayer.setOnEndOfMedia(() -> gameMusicMediaPlayer.seek(Duration.ZERO));


        String fxMediaLocation = Music.NEON.getPath();
        Media fxMedia = new Media(new File(fxMediaLocation).toURI().toString());
        fxMediaPlayer = new MediaPlayer(fxMedia);
        fxMediaPlayer.setOnEndOfMedia(() -> fxMediaPlayer.seek(Duration.ZERO));


        menuMusicMediaPlayer.play();
        fxMediaPlayer.play();

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
        musicVolume = volume;
        menuMusicMediaPlayer.setVolume(volume);
        gameMusicMediaPlayer.setVolume(volume);
    }


    public void setGameMusic(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(menuMusicMediaPlayer.volumeProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> menuMusicMediaPlayer.stop()));
        timeline.play();

        gameMusicMediaPlayer.setVolume(musicVolume);
        gameMusicMediaPlayer.play();
    }

    public void setMenuMusic(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(gameMusicMediaPlayer.volumeProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> gameMusicMediaPlayer.stop()));
        timeline.play();

        menuMusicMediaPlayer.setVolume(musicVolume);
        menuMusicMediaPlayer.play();
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
