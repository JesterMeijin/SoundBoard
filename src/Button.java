import javax.swing.*;
import java.awt.*;
import java.io.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener {
    private String filename;
    private String ext;
    private PlaySound sound;

    public Button(String displayName, String filename){
        super(displayName);
        this.setBackground(new Color(245,245,245));
        this.setFont(new Font("Roboto Medium", Font.PLAIN, 20));
        this.setForeground(new Color(33,33,33));
        this.filename = filename;
        this.ext = "." + filename.substring(filename.lastIndexOf(".") + 1);
        if (!this.ext.equals(".mp3"))
            this.sound = new PlaySound(this.filename);
        this.addMouseListener(this);
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

    public void setSound(){
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
        if (this.isEnabled()){
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
    }

    private void renameSound(String filename){
        sound.stopSound();
        JFrame frame = new JFrame();
        File file = new File(filename);

        String name = JOptionPane.showInputDialog(frame, "What's the new sound name ?", "Rename Sound", JOptionPane.QUESTION_MESSAGE);
        if ((name != null) && (name.length() > 0)){
            this.setText(name);
            this.filename = "sounds/" + name + ext;

            if (file.renameTo(new File("sounds/" + name + ext)))
                System.out.println("Rename successful");
            else
                System.out.println("Rename failed");

            this.setSound();
        }
    }

    private void removeSound(String filename){
        sound.stopSound();
        JFrame frame = new JFrame();

        int input = JOptionPane.showConfirmDialog(frame, "Do you really want to remove the sound ?\n Warning : It will delete the sound from the sounds folder too !", " Delete Sound ", JOptionPane.YES_NO_OPTION);
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
