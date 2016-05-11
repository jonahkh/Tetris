/*
 * Audio Clip Player
 * TCSS 305
 */

package view;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.pscode.xui.sound.bigclip.BigClip;

/**
 * A class to play audio clips. Caches previously-played clips,
 * allowing fast re-playback of previously played sounds.
 * 
 * @author Anon
 * @author Jonah Howard added class constants representing file names and constructor that pre
 *                      loads all of the audio files
 * @version 1.51
 */

public class SoundPlayer {
    
    /** The filename of the move sound. */
    public static final String MOVE = "./extras/move.wav";
    
    /** The filename of the rotate sound. */
    public static final String ROTATE = "./extras/rotate.wav";

    /** The filename of the hard drop sound. */
    public static final String HARD_DROP = "./extras/hard_drop.wav";
    
    /** The filename of the theme song. */
    public static final String THEME_SONG = "./extras/smbtheme.wav";
    
    /** The filename of the clear line sound. */
    public static final String CLEAR_LINE = "./extras/clear_line.wav";
    
    /** The filename of the pause sound. */
    public static final String PAUSE_SOUND = "./extras/pause.wav";
    
    /** The filename of the level up sound. */
    public static final String LEVEL_UP = "./extras/level_up.wav";
    
    /** The filename of the controls menu confirmation sound. */
    public static final String CHANGE_CONTROLS = "./extras/controls.wav";
    
    /** The sound that plays when exiting the game before starting. */
    public static final String EXIT_GAME = "./extras/mamma_mia.wav";
    
    /** The sound that plays when exiting the game after losing a game. */
    public static final String EXIT_GAME_END = "./extras/goodbye.wav";
    
    /** The sound that plays when the user loses a game. */
    public static final String GAME_OVER = "./extras/game_over.wav";
    
    /** The sound that plays when the user chooses to play another game. */
    public static final String NEW_GAME = "./extras/yippee.wav";
    
    /** A cache of previously-played audio clips. */
    private final Map<String, Clip> myClips = new HashMap<String, Clip>();
    
    /** Determines if the sounds have been muted or not. */
    private boolean myMuted;

    /**
     * Initializes a new SoundPlayer.
     */
    public SoundPlayer() {
        myMuted = false;
        preLoadFiles();
    }
    
    /**
     * Pre-loads all of the audio files.
     */
    private void preLoadFiles() {
        preLoad(MOVE);
        preLoad(ROTATE);
        preLoad(HARD_DROP);
        preLoad(THEME_SONG);
        preLoad(GAME_OVER);
        preLoad(LEVEL_UP);
        preLoad(PAUSE_SOUND);
        preLoad(CHANGE_CONTROLS);
        preLoad(EXIT_GAME);
        preLoad(EXIT_GAME_END);
        preLoad(NEW_GAME);
        preLoad(CLEAR_LINE);
    }
    
    /**
     * Sets whether or not the sounds are muted.
     * 
     * @param theState whether or not the sounds are to be muted. True if so
     */
    protected void mute(final boolean theState) {
        myMuted = theState;
    }
    
    /**
     * Returns whether the sounds are muted or not.
     * 
     * @return if all sounds are muted
     */
    protected boolean isMuted() {
        return myMuted;
    }
    
    /**
     * Plays the audio file with the given file name.
     * This method returns instantly, without waiting for the clip to finish playing.
     * 
     * @param theFilename The name of the file to play.
     * @return a Clip object representing the sound played.
     * @throws IllegalArgumentException if there is a problem reading from the sound file.
     */
    public Clip play(final String theFilename) throws IllegalArgumentException {
        return loop(theFilename, 1);
    }
    
    /** 
     * Plays the clip with the given file name in a continuous loop.
     * The clip keeps looping until it is later stopped by calling the 
     * stop() method. This function returns instantly
     *    
     * @param theFilename The name of the file to play.
     * @return a Clip object representing the sound played.
     * @throws IllegalArgumentException if there is a problem reading from the sound file.
     */
    public Clip loop(final String theFilename) throws IllegalArgumentException {
        return loop(theFilename, Clip.LOOP_CONTINUOUSLY);
    }
    
    
    /** 
     * Plays the clip with the given file name in a loop.
     * The clip loops until it has played the specified number of times,
     * or until it is later stopped by calling the stop() method.
     * This function returns instantly, without waiting for the clip to finish looping.
     *
     * @param theFilename The name of the file to play.
     * @param theNumberOfTimes The number of times to loop the clip.
     * @return a Clip object representing the sound played.
     * @exception IllegalArgumentException if there is a problem reading from the sound file.
     */
    public Clip loop(final String theFilename, final int theNumberOfTimes) 
        throws IllegalArgumentException {
        
        final Clip clip = getClip(theFilename);
        
        if (clip != null) {
            clip.loop(theNumberOfTimes);
        }
        
        return clip;
    }
    
    /**
     * Pauses the clip with the given file name.
     * If the clip is later played, it will resume from where it was paused.
     * Calling this method does not resume a thread that is 
     * suspended on a playAndWait() or a loopAndWait().
     * 
     * If stop() is called on a paused clip, it will reset to the
     * beginning of the clip for the next play.
     * 
     * @param theFilename The name of the file to pause.
     * @exception IllegalArgumentException if there is a problem reading from
     * or playing the sound file.
     */
    public void pause(final String theFilename) throws IllegalArgumentException {
        final Clip clip = getClip(theFilename);
        
        if (clip != null) {
            final int pos = clip.getFramePosition();
            clip.stop();
            clip.setFramePosition(pos);
        }
    }
    
    /** 
     * Stops the clip with the specified filename
     * (and wakes up any threads waiting for it to finish playing).
     * 
     * @param theFilename The name of the file to stop.
     * @return a Clip object representing the sound stopped.
     * @exception IllegalArgumentException if there is a problem reading from the sound file.
     */
    public Clip stop(final String theFilename) 
        throws IllegalArgumentException {
        final Clip clip = getClip(theFilename);
        stopClip(clip);
        return clip;
    }
    
    /** 
     * Stops all currently playing sound clips
     * (and wakes up the threads waiting for them to finish playing).
     */
    public void stopAll() {
        for (final Clip clip : myClips.values()) {
            stopClip(clip);
        }
    }   

    /** 
     * Preloads the clip at the given file name.
     * This means the clip will be available faster, when requested for playing the first time.
     * @param theFilename The name of the file to preload.
     * @return a Clip object representing the preloaded sound.
     * @exception IllegalArgumentException if there is a problem reading from the sound file.
     */
    public Clip preLoad(final String theFilename) 
        throws IllegalArgumentException {
        return getClip(theFilename);
    }
    

    /**
     * Returns a Clip object for a filename, either by creating
     * a new one or loading it from the cache.
     * 
     * @param theFilename The name of the file to load.
     * @return a Clip object, or null if one is not found.
     * @exception IllegalArgumentException if there is a problem reading from the sound file.
     */
    private Clip getClip(final String theFilename) throws IllegalArgumentException {
        BigClip clip = null;
        AudioInputStream ais = null;
            
        if (myClips.containsKey(theFilename)) {
            clip = (BigClip) myClips.get(theFilename);
        } else {
            // read audio file from disk
            try {
                ais = AudioSystem.getAudioInputStream(new File(theFilename));
                clip = new BigClip();
                clip.open(ais);
                clip.addLineListener(new LineListener() {
                    /** 
                     * Responds to audio events generated by clips. 
                     * 
                     * @param theEvent The event generated.
                     */
                    public void update(final LineEvent theEvent) {
                        if (theEvent.getType() == LineEvent.Type.STOP) {
                            // clip is done playing
                            stopClip((Clip) theEvent.getSource());
                        }
                    }
                });
                myClips.put(theFilename, clip);
            } catch (final UnsupportedAudioFileException uafe) {
                throw new IllegalArgumentException
                ("Not a valid supported audio file: \"" + theFilename + "\"", uafe);
            } catch (final LineUnavailableException lue) {
                lue.printStackTrace();
                throw new IllegalArgumentException
                ("Line is not available to play sound \"" + theFilename + " \"", lue);
            } catch (final IOException ioe) {
                throw new IllegalArgumentException
                ("I/O error while reading file: \"" + theFilename + "\" ", ioe);
            }
        }
            
        return clip;
    }
    
    /**
     * Stops the playing of the specified clip.
     * 
     * @param theClip The clip.
     */
    private void stopClip(final Clip theClip) {
        if (theClip != null) {
            synchronized (theClip) {
                theClip.stop();
                theClip.setFramePosition(0);
                theClip.notifyAll();  // awaken threads waiting for this Clip
            }
        }
    }
}

// end of class SoundPlayer
