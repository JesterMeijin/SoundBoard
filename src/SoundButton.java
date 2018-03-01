import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SoundButton extends JButton implements MouseListener {
    private String filename;
    private String ext;
    private SoundPlayer soundPlayer;

    SoundButton(String displayName, String filename){
        super(displayName);
        this.setBackground(new Color(245,245,245));
        this.setFont(new Font("Roboto Medium", Font.PLAIN, 20));
        this.setForeground(new Color(33,33,33));
        this.filename = filename;
        this.ext = "." + filename.substring(filename.lastIndexOf(".") + 1);
        if (!this.ext.equals(".mp3"))
            this.soundPlayer = new SoundPlayer(this.filename);
        this.addMouseListener(this);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (this.isEnabled()){
            if (SwingUtilities.isLeftMouseButton(e))
                playSound();
            if (SwingUtilities.isRightMouseButton(e))
                renameSound(filename);
            if (SwingUtilities.isMiddleMouseButton(e))
                removeSound(filename);
        }
    }

    public void setFilename (String newFilename){
        this.filename = newFilename;
    }

    public String readFilename (){
        return this.filename;
    }

    public void setExt (String newExt){
        this.ext = newExt;
    }

    public String readExt (){
        return this.ext;
    }

    public void rebuildSoundPlayer(){
        soundPlayer = new SoundPlayer(this.filename);
    }

    private void playSound(){
        if (soundPlayer.isRunning())
            soundPlayer.stopSound();
        else
            soundPlayer.startSound();
    }

    private void renameSound(String filename){
        soundPlayer.stopSound();
        JFrame frame = new JFrame();
        File file = new File(filename);

        String name = JOptionPane.showInputDialog(frame, "What's the new soundPlayer name ?", "Rename SoundManager", JOptionPane.QUESTION_MESSAGE);
        if ((name != null) && (name.length() > 0)){
            this.setText(name);
            this.filename = "sounds/" + name + ext;

            if (file.renameTo(new File("sounds/" + name + ext)))
                System.out.println("Rename successful");
            else
                System.out.println("Rename failed");

            this.rebuildSoundPlayer();
        }
    }

    private void removeSound(String filename){
        soundPlayer.stopSound();
        JFrame frame = new JFrame();

        int input = JOptionPane.showConfirmDialog(frame, "Do you really want to remove the soundPlayer ?\n Warning : It will delete the soundPlayer from the sounds folder too !", " Delete SoundManager ", JOptionPane.YES_NO_OPTION);
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
