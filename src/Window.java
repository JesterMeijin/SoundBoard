import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Window extends JFrame implements ActionListener {

    private JFileChooser fc = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio files", "mp3", "wav", "ogg", "aiff", "voc", "mid");
    private JPanel mainPanel = new JPanel();
    private GridBagLayout mainLayout = new GridBagLayout();
    private GridBagConstraints layoutConstraints = new GridBagConstraints();
    private JButton addButton = new JButton("+");
    private int songNumber = 0;

    public Window(){

        //Set window properties
        this.setTitle("SoundBoard");
        this.setSize(720, 480);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the layout constraints
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        layoutConstraints.gridx = 1;
        mainLayout.setConstraints(addButton, layoutConstraints);

        //Set the panel
        mainPanel.setBackground(Color.BLACK);
        mainPanel.setLayout(mainLayout);
        this.setContentPane(mainPanel);

        //Set the add button
        addButton.addActionListener(this);
        mainPanel.add(addButton);

        //Set the window visible
        this.setVisible(true);
        initSongList();
    }

    public void actionPerformed(ActionEvent arg0) {
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
            Button songButton = addSong(fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""), fc.getSelectedFile().getName(), fc.getSelectedFile().getAbsolutePath());
            CopyBinaryFileWithStreams( fc.getSelectedFile().getAbsolutePath(), fc.getSelectedFile().getName());
            songButton.editFilename("song/" + fc.getSelectedFile().getName());
        }
    }

    public Button addSong (String displayName, String fileBasename, String filename){
        Button songButton = new Button(displayName, fileBasename, filename);
        mainLayout.setConstraints(songButton, layoutConstraints);
        mainPanel.add(songButton);
        mainPanel.revalidate();
        return songButton;
    }

    public void initSongList() {
        File folder = new File("song");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                initSongList();
            } else {
                addSong(fileEntry.getName().replaceFirst("[.][^.]+$", ""), fileEntry.getName(), "song/" + fileEntry.getName());
            }
        }
    }

    public void CopyBinaryFileWithStreams(String sourceFile, String fileBasename) {

        String destFile = "song/" + fileBasename;
        FileInputStream fis = null;
        FileOutputStream fos = null;

        try {
            fis = new FileInputStream(sourceFile);
            fos = new FileOutputStream(destFile);

            byte[] buffer = new byte[1024];
            int noOfBytes = 0;

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
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            }
            catch (IOException ioe) {
                System.out.println("Error while closing stream: " + ioe);
            }
        }
    }

}