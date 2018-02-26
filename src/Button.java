import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener {
    private String filename;
    private String fileBasename;
    private String ext;
    private PlaySound sound;

    public Button(String displayName, String fileBasename, String filename){
        super(displayName);
        this.setBackground(new Color(238,238,238));
        this.setFont(new Font("Roboto", Font.PLAIN, 20));
        this.setForeground(Color.BLACK);
        this.filename = filename;
        this.fileBasename = fileBasename;
        this.ext = "." + filename.substring(filename.lastIndexOf(".") + 1);
        this.sound = new PlaySound(this.filename);
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

    public String readExt (){
        return this.ext;
    }

    public void updateSong(){
        sound = new PlaySound(this.filename);
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
        if (SwingUtilities.isLeftMouseButton(e)){
            if (sound.readClip().isRunning())
                sound.stopSound();
            else
                sound.startSound();
        }
        if (SwingUtilities.isRightMouseButton(e))
            renameSound(filename);
        if (SwingUtilities.isMiddleMouseButton(e))
            removeSound(filename);
    }

    public void renameSound(String filename){
        sound.stopSound();
        JFrame frame = new JFrame();
        File file = new File(filename);

        String name = JOptionPane.showInputDialog(frame, "What's the new song name ?", "Rename Sound", JOptionPane.QUESTION_MESSAGE);
        if ((name != null) && (name.length() > 0)){
            this.setText(name);
            this.filename = "song/" + name + ext;
            this.fileBasename = name + ext;

            if (file.renameTo(new File("song/" + name + ext)))
                System.out.println("Rename successful");
            else
                System.out.println("Rename failed");

            this.updateSong();
        }
    }

    public void removeSound(String filename){
        sound.stopSound();
        JFrame frame = new JFrame();

        int input = JOptionPane.showConfirmDialog(frame, "Do you really want to remove the sound ?\n Warning : It will delete the sound from the song folder too !", " Delete Sound ", JOptionPane.YES_NO_OPTION);
        if (input == JOptionPane.OK_OPTION){
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
}
