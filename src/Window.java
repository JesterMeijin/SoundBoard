import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.JavaLayerException;
import mdlaf.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class Window extends JFrame implements ActionListener {

    private JFileChooser fc = new JFileChooser();
    private FileNameExtensionFilter filter = new FileNameExtensionFilter("Audio files", "wav", "aiff", "au", "mid", "midi", "mp3");
    private JPanel mainPanel = new JPanel();
    private JPanel buttonPanel = new JPanel();
    private JScrollPane scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    private JScrollBar bar = scrollPane.getVerticalScrollBar();
    private GridBagLayout mainLayout = new GridBagLayout();
    private GridLayout buttonLayout = new GridLayout(0,1);
    private GridBagConstraints layoutConstraints = new GridBagConstraints();
    private GridBagConstraints addButtonLayoutConstraints = new GridBagConstraints();
    private JButton addButton = new JButton("+");
    private MaterialUIMovement animate = new MaterialUIMovement (new Color (224,224,224), 5, 1000 / 30);
    private MaterialUIMovement animate2 = new MaterialUIMovement (new Color (189,189,189), 5, 1000 / 30);

    public Window(){

        //Set window properties
        this.setTitle("SoundBoard");
        this.setSize(400, 500);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel (new MaterialLookAndFeel ());
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace ();
        }

        //Set addButton layout constraints
        addButtonLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        mainLayout.setConstraints(addButton, addButtonLayoutConstraints);

        //Set the layout constraints
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        layoutConstraints.gridy = 1;
        mainLayout.setConstraints(scrollPane, layoutConstraints);

        //Set mainPanel
        mainPanel.setBackground(new Color (238,238,238));
        mainPanel.setLayout(mainLayout);
        this.setContentPane(mainPanel);

        //Set the add button
        addButton.setBackground(new Color (224,224,224));
        addButton.setFont(new Font("Roboto", Font.BOLD, 30));
        addButton.setForeground(new Color (0,230,118));
        addButton.setBorderPainted(false);
        addButton.addActionListener(this);
        mainPanel.add(addButton);
        animate2.add(addButton);

        //Set the buttonPanel / scrollPane
        bar.setPreferredSize(new Dimension(0, 0));
        bar.setUnitIncrement(16);
        buttonPanel.setBackground(new Color (238,238,238));
        buttonPanel.setPreferredSize(new Dimension(0, 500));
        buttonPanel.setLayout(buttonLayout);
        mainPanel.add(scrollPane);

        //Set the window visible
        this.setVisible(true);
        initSongList();
    }

    public void actionPerformed(ActionEvent arg0) {
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
            boolean checkMP3 = new File("song/", fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ".wav")).exists();
            boolean check = new File("song/", fc.getSelectedFile().getName()).exists();
            if (!check && !checkMP3) {
                Button songButton = addSound(fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""), fc.getSelectedFile().getName(), fc.getSelectedFile().getAbsolutePath());
                CopyBinaryFileWithStreams( fc.getSelectedFile().getAbsolutePath(), fc.getSelectedFile().getName());
                songButton.editFilename("song/" + fc.getSelectedFile().getName());
                if (songButton.readExt().equals(".mp3"))
                    convertMp3toWav(songButton);
            } else {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "The song's file has already been added to the SoundBoard !", " Import Error ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public Button addSound(String displayName, String fileBasename, String filename){
        Button songButton = new Button(displayName, fileBasename, filename);
        buttonPanel.add(songButton);
        animate.add(songButton);
        buttonPanel.revalidate();
        return songButton;
    }

    public void initSongList() {
        File folder = new File("song");
        for (File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                initSongList();
            } else {
                addSound(fileEntry.getName().replaceFirst("[.][^.]+$", ""), fileEntry.getName(), "song/" + fileEntry.getName());
            }
        }
    }

    public void convertMp3toWav(Button songButton){
        try {
            String newFilename;
            File file = new File(songButton.readFilename());
            Converter converter = new Converter();
            newFilename = "song/" + file.getName().replaceFirst("[.][^.]+$", ".wav");
            converter.convert(songButton.readFilename(), newFilename);
            if (file.delete())
                System.out.println(file.getName() + " is deleted!");
            else
                System.out.println("Delete operation is failed.");
            songButton.editFilename(newFilename);
            songButton.updateSong();
        } catch (JavaLayerException e){
            e.printStackTrace();
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