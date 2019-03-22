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

/**
 * Class that handles the playing of all audio
 */
public class AudioManager {

    /**
     *  The volume of all sound effects
     */
    private static double effectVolume;

    /**
     * The volume of all music tracks
     */
    private static double musicVolume;

    /**
     * A HashMap containing each spot effect enum and its relative AudioClip
     */
    private HashMap<Sound, AudioClip> gameEffects;

    /**
     *  MediaPlayer object for the menu music
     */
    private MediaPlayer menuMusicMediaPlayer;

    /**
     *  MediaPlayer object for the game music
     */
    private MediaPlayer gameMusicMediaPlayer;

    /**
     *  MediaPlayer object for the neon hum track
     */
    private MediaPlayer fxMediaPlayer;


    /**
     *  Creates an AudioManager object
     *  This loads all the sound files into the object and sets both volumes to 100 by default
     */
    public AudioManager(){
        effectVolume = 100;
        musicVolume = 100;
        gameEffects = new HashMap<>();

        // loop through the sound enum, adding an AudioClip for each to the HashMap
        // AudioClips are used as they allow for easy playback than MediaPlayers
        for(Sound sound: Sound.values()){
            String location = sound.getPath();
            AudioClip audioClip = new AudioClip(new File(location).toURI().toString());
            gameEffects.put(sound, audioClip);
        }

        initialiseGameMusic();
    }

    /**
     *  Method that initialises each of the music tracks, loading them into the AudioManager
     *  After loading, starts both the menu music and the neon effect
     *  Sets each music track to loop indefinitely
     */
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

    /**
     * Plays a sound
     * @param sound The sound to play
     */
    public void playSound(Sound sound){
        gameEffects.get(sound).play(effectVolume);
    }

    /**
     * Plays a sound at a known distance from the player
     * The volume is the distance value multiplied by the current volume
     * @param sound The sound to be played
     * @param volumeDistance    The 'distance' at which the sound happens, between 0 and 1
     */
    private void playSound(Sound sound, double volumeDistance) {
        gameEffects.get(sound).play(volumeDistance * effectVolume);
    }

    /**
     * Sets the volume of the effects
     * @param volume The desired volume
     */
    public void setEffectVolume(double volume) {
        AudioManager.effectVolume = volume;
        fxMediaPlayer.setVolume(volume);
    }

    /**
     * Sets the volume of the neon hum
     * @param volume The desired volume
     */
    public void setNeonVolume(double volume) {
        fxMediaPlayer.setVolume(volume);
    }


    /**
     *  Returns the volume of the sound effects
     * @return The volume of the sound effects
     */
    public double getEffectVolume() {
        return effectVolume;
    }

    /**
     * Sets the volume of the music tracks
     * @param volume The desired volume
     */
    public void setMusicVolume(double volume) {
        musicVolume = volume;
        menuMusicMediaPlayer.setVolume(volume);
        gameMusicMediaPlayer.setVolume(volume);
    }

    /**
     *  Sets the current music to the game theme
     *  Fades out the menu theme
     */
    public void setGameMusic(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(menuMusicMediaPlayer.volumeProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> menuMusicMediaPlayer.stop()));
        timeline.play();

        gameMusicMediaPlayer.setVolume(musicVolume);
        gameMusicMediaPlayer.play();
    }

    /**
     *  Sets the current music to the menu theme
     *  Fades out the game theme
     */
    public void setMenuMusic(){
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(2), new KeyValue(gameMusicMediaPlayer.volumeProperty(), 0)),
                new KeyFrame(Duration.seconds(3), e -> gameMusicMediaPlayer.stop()));
        timeline.play();

        menuMusicMediaPlayer.setVolume(musicVolume);
        menuMusicMediaPlayer.play();
    }

    /**
     * The function which, when called every client tick, handles the playing of spot effects
     * Uses the GameState to calculate the distance at which effects are sounded
     * @param gameState The current GameState object
     */
    // todo improve this
    public void clientLoop(ClientGameState gameState) {

        // only play sounds if the player is alive
        if(gameState.getPlayer().isAlive()) {

            // check whether player is performing an action and whether it has already sounded
            if(gameState.getPlayer().getCurrentAction() != Action.IDLE && !gameState.getPlayer().hasActionSounded()) {
                playSound(Sound.switchSound(gameState.getPlayer().getCurrentAction()));
                gameState.getPlayer().setActionHasSounded(true);
            }

            // do the same for each enemy
            for(Player enemy: gameState.getOtherPlayers(gameState.getPlayer())) {

                if(enemy.getCurrentAction() != Action.IDLE && !enemy.hasActionSounded()) {

                    // calculate the 'distance' of the sound from the player
                    double distance = gameState.getPlayer().getLocation().distance(enemy.getLocation());
                    double func = 1 / (distance);

                    playSound(Sound.switchSound(enemy.getCurrentAction()), func);
                    enemy.setActionHasSounded(true);
                }

            }
        }


    }
}
