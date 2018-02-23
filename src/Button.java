import javax.sound.sampled.*;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Button extends JButton implements ActionListener {
    private String filename;
    private String fileBasename;
    private String displayName;

    public Button(String displayName, String fileBasename, String filename){
        super(displayName);
        this.filename = filename;
        this.fileBasename = fileBasename;
        this.displayName = displayName;
        this.addActionListener(this);
    }

    public void editFilename (String newFilename){
        this.filename = newFilename;
    }

    public void editFileBasename (String newFileBasename){
        this.fileBasename = newFileBasename;
    }

    public void editDisplayname (String newDisplayname){
        this.displayName = newDisplayname;
    }

    public String readFilename (){
        return this.filename;
    }

    public String readFileBasename (){
        return this.fileBasename;
    }

    public String readDisplayname (){
        return this.displayName;
    }

    public void actionPerformed(ActionEvent arg0) {
        PlaySound(filename);
    }

    public void PlaySound (String filename){
        try {
            // Open an audio input stream.
            File soundFile = new File(filename); //you could also get the sound file with an URL
            System.out.println("Playing sound : " + filename);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundFile);
            // Get a sound clip resource.
            Clip clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
