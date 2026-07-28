package com.example.synthesizer;
import javax.sound.sampled.*;

import static javax.sound.sampled.AudioSystem.getClip;

public class SoundOutput {
    public static void play(AudioClip source) throws LineUnavailableException {
        Clip c = AudioSystem.getClip();// Your code

        // This is the format that we're following, 44.1 KHz mono audio, 16 bits per sample.
        AudioFormat format16 = new AudioFormat( 44100, 16, 1, true, false );

        c.open( format16, source.getData(), 0, source.getData().length ); // Reads data from our byte array to play it.

        System.out.println( "About to play..." );
        c.start(); // Plays it.
        c.loop( 2 ); // Plays it 2 more times if desired, so 6 seconds total

// Makes sure the program doesn't quit before the sound plays.
        while(c.getFramePosition() < AudioClip.FULL_SAMPLE_RATE || c.isActive() || c.isRunning() ){
            // Do nothing while we wait for the note to play.
        }

        System.out.println( "Done." );
        c.close();
    }
}
