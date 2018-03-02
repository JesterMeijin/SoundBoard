import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class SoundButton extends JButton implements MouseListener {

    private String filename;
    private String ext;
    private SoundPlayer soundPlayer;
    private SoundManager soundManager;

    SoundButton(SoundManager soundManager, String displayName, String filename){

        super(displayName);

        this.soundManager = soundManager;
        this.filename = filename;
        this.ext = "." + filename.substring(filename.lastIndexOf(".") + 1);
        if (!this.ext.equals(".mp3"))
            this.soundPlayer = new SoundPlayer(this.filename);

        setBackground(new Color(245,245,245));
        setFont(new Font("Roboto Medium", Font.PLAIN, 20));
        setForeground(new Color(33,33,33));

        addMouseListener(this);
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

        if (isEnabled()){
            if (SwingUtilities.isLeftMouseButton(e))
                playSound();
            if (SwingUtilities.isRightMouseButton(e))
                soundManager.renameSound(filename, this);
            if (SwingUtilities.isMiddleMouseButton(e))
                soundManager.removeSound(filename, this);
        }
    }

    public void setFilename (String newFilename){
        filename = newFilename;
    }

    public String readFilename (){
        return filename;
    }

    public void setExt (String newExt){
        ext = newExt;
    }

    public String getExt (){
        return ext;
    }

    public String readExt (){
        return ext;
    }

    public void rebuildSoundPlayer(){
        soundPlayer = new SoundPlayer(filename);
    }

    public SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    private void playSound(){

        if (soundPlayer.isRunning())
            soundPlayer.stopSound();
        else
            soundPlayer.startSound();
    }
}
