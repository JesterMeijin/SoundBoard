import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import mdlaf.MaterialUIMovement;

import javax.swing.*;
import java.io.*;
import java.util.Objects;

class Sound {

    private final JPanel buttonPanel;
    private final Search search;
    private final MaterialUIMovement animate;

    Sound(JPanel buttonPanel, Search search, MaterialUIMovement animate){
        this.buttonPanel = buttonPanel;
        this.search = search;
        this.animate = animate;
    }

    public void initSoundList() {
        File folder = new File("sounds");
        for (File fileEntry : Objects.requireNonNull(folder.listFiles())) {
            if (fileEntry.isDirectory()) {
                initSoundList();
            } else {
                Button soundButton = addSound(fileEntry.getName().replaceFirst("[.][^.]+$", ""), "sounds/" + fileEntry.getName());
                if (soundButton.readExt().equals(".mp3"))
                    convertMp3toWav(soundButton);
            }
        }
    }

    public Button addSound(String displayName, String filename){
        Button soundButton = new Button(displayName, filename);
        buttonPanel.add(soundButton);
        animate.add(soundButton);
        buttonPanel.revalidate();
        search.setComponents(buttonPanel.getComponents());
        return soundButton;
    }

    public void convertMp3toWav(Button soundButton){
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
            soundButton.setSound();
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
