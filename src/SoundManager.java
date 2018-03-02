import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import mdlaf.MaterialUIMovement;
import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class SoundManager {

    private JPanel buttonPanel;
    private List<SoundButton> soundButtons;
    private final MaterialUIMovement animate;

    SoundManager(JPanel buttonPanel, MaterialUIMovement animate){

        this.buttonPanel = buttonPanel;
        this.animate = animate;
        this.soundButtons = new ArrayList<>();
    }

    public void initSoundList() {

        File folder = new File("sounds");

        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                initSoundList();
            } else {
                SoundButton soundButton = addSound(fileEntry.getName().replaceFirst("[.][^.]+$", ""), "sounds/" + fileEntry.getName());
                if (soundButton.readExt().equals(".mp3"))
                    convertMp3toWav(soundButton);
            }
        }
    }

    public SoundButton addSound(String displayName, String filename){

        SoundButton soundButton = new SoundButton(this, displayName, filename);

        buttonPanel.add(soundButton);
        animate.add(soundButton);
        buttonPanel.revalidate();
        soundButtons.add(soundButton);

        return soundButton;
    }

    public void renameSound(String filename, SoundButton soundButton){

        soundButton.getSoundPlayer().stopSound();

        JFrame frame = new JFrame();
        File file = new File(filename);
        String name = JOptionPane.showInputDialog(frame, "What's the new soundPlayer name ?", "Rename SoundManager", JOptionPane.QUESTION_MESSAGE);

        if ((name != null) && (name.length() > 0)){
            soundButton.setText(name);
            soundButton.setFilename("sounds/" + name + soundButton.getExt());

            if (file.renameTo(new File("sounds/" + name + soundButton.getExt())))
                System.out.println("Rename successful");
            else
                System.out.println("Rename failed");

            soundButton.rebuildSoundPlayer();
        }
    }

    public void removeSound(String filename, SoundButton soundButton){

        soundButton.getSoundPlayer().stopSound();

        JFrame frame = new JFrame();
        int input = JOptionPane.showConfirmDialog(frame, "Do you really want to remove the soundPlayer ?\n Warning : It will delete the soundPlayer from the sounds folder too !", " Delete SoundManager ", JOptionPane.YES_NO_OPTION);

        if (input == JOptionPane.OK_OPTION){
            File file = new File(filename);

            if(file.delete())
                System.out.println(file.getName() + " is deleted!");
            else
                System.out.println("Delete operation is failed.");

            soundButtons.remove(soundButton);
            soundButton.setVisible(false);
            soundButton.getParent().revalidate();
            soundButton.getParent().remove(soundButton);
        }
    }

    public List<SoundButton> getSoundButtons(){
        return soundButtons;
    }

    public void convertMp3toWav(SoundButton soundButton){

        try {
            String newFilename;
            File file = new File(soundButton.readFilename());
            Converter converter = new Converter();
            newFilename = "sounds/" + file.getName().replaceFirst("[.][^.]+$", ".wav");
            converter.convert(soundButton.readFilename(), newFilename);

            if (file.delete())
                System.out.println(file.getName() + " is deleted!");
            else
                System.out.println("Delete operation is failed.");

            soundButton.setFilename(newFilename);
            soundButton.setExt(".wav");
            soundButton.rebuildSoundPlayer();

        } catch (JavaLayerException e){
            e.printStackTrace();
        }
    }

    public void CopyBinaryFileWithStreams(String sourceFile, String fileBasename) {

        String destFile = "sounds/" + fileBasename;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int noOfBytes;

            System.out.println("Copying file using streams");

            // read bytes from source file and write to destination file
            while ((noOfBytes = fis.read(buffer)) != -1) {
                fos.write(buffer, 0, noOfBytes);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found" + e);
        }
        catch (IOException ioe) {
            System.out.println("Exception while copying file " + ioe);
        }
        finally {
            // close the streams using close method
            try {
                if (fis != null)
                    fis.close();
                if (fos != null)
                    fos.close();
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }
}
