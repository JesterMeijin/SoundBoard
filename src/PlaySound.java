import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

class PlaySound {

    private Clip clip;

    public PlaySound (String filename) {
        try {
            // Open an audio input stream.
            File soundFile = new File(filename); //you could also get the sound file with an URL
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
        } catch(UnsupportedAudioFileException | LineUnavailableException | IOException e){
            e.printStackTrace();
        }
    }

    public Clip readClip() {
        return this.clip;
    }

    public void stopSound(){
        clip.stop();
    }

    public void startSound(){
        clip.setFramePosition(0);
        clip.start();
    }
}
