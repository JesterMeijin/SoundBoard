import javax.sound.sampled.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener {
    private String filename;
    private String fileBasename;

    public Button(String displayName, String fileBasename, String filename){
        super(displayName);
        this.filename = filename;
        this.fileBasename = fileBasename;
        this.addMouseListener(this);
    }

    public void editFilename (String newFilename){
        this.filename = newFilename;
    }

    public void editFileBasename (String newFileBasename){
        this.fileBasename = newFileBasename;
    }

    public String readFilename (){
        return this.filename;
    }

    public String readFileBasename (){
        return this.fileBasename;
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e))
            playSound(filename);
        if (SwingUtilities.isRightMouseButton(e))
            renameSound(filename);
        if (SwingUtilities.isMiddleMouseButton(e))
            removeSound(filename);
    }

    public void playSound (String filename){
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

    public void renameSound(String filename){
        JFrame frame = new JFrame();
        File file = new File(filename);
        String ext = "." + filename.substring(filename.lastIndexOf(".")+1);

        String name = JOptionPane.showInputDialog(frame, "What's the new song name ?", "Rename Song", JOptionPane.QUESTION_MESSAGE);
        this.setText(name);
        this.filename = "song/" + name + ext;
        this.fileBasename = name  + ext;

        if(file.renameTo(new File("song/" + name  + ext)))
            System.out.println("Rename successful");
        else
            System.out.println("Rename failed");
    }

    public void removeSound(String filename){
        File file = new File(filename);
        if(file.delete())
            System.out.println(file.getName() + " is deleted!");
        else
            System.out.println("Delete operation is failed.");
        this.setVisible(false);
        this.getParent().revalidate();
        this.getParent().remove(this);
    }
}
