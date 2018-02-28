import mdlaf.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;


class Window extends JFrame implements ActionListener {

    private final JFileChooser fc;
    private final FileNameExtensionFilter filter;
    private final Sound sound;

    Window(){

        /* Initializes variables */
        fc = new JFileChooser();
        filter = new FileNameExtensionFilter("Audio files", "wav", "aiff", "au", "mid", "midi", "mp3");
        JPanel topPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagLayout mainLayout = new GridBagLayout();
        GridBagLayout topLayout = new GridBagLayout();
        GridLayout buttonLayout = new GridLayout(0,1);
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        GridBagConstraints topLayoutConstraints = new GridBagConstraints();
        JButton addButton = new JButton("+");
        Search search = new Search("Search", buttonPanel);
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        MaterialUIMovement animate = new MaterialUIMovement (new Color (224,224,224), 5, 1000 / 30);
        MaterialUIMovement animate2 = new MaterialUIMovement (new Color (0,191,165), 5, 1000 / 30);
        sound = new Sound(buttonPanel, search, animate);

        /* Sets Window properties */
        this.setTitle("SoundBoard");
        this.setSize(350, 500);
        this.setLayout(mainLayout);
        this.setLocationRelativeTo(null);
        this.setBackground(new Color (238,238,238));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            UIManager.setLookAndFeel (new MaterialLookAndFeel ());
        }
        catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace ();
        }

        /* Sets topPanel constraints */
        topLayoutConstraints.fill = GridBagConstraints.HORIZONTAL;
        topLayoutConstraints.weightx = 1;
        mainLayout.setConstraints(topPanel, topLayoutConstraints);
        topLayout.setConstraints(search, topLayoutConstraints);

        /* Sets scrollPane constraints */
        layoutConstraints.fill = GridBagConstraints.BOTH;
        layoutConstraints.weightx = 1;
        layoutConstraints.weighty = 1;
        layoutConstraints.gridy = 1;
        mainLayout.setConstraints(scrollPane, layoutConstraints);

        /* Sets topPanel properties */
        topPanel.setLayout(topLayout);
        topPanel.setBackground(new Color (29,233,182));
        topPanel.add(search);
        topPanel.add(addButton);
        this.add(topPanel);

        /* Sets addButton properties */
        addButton.setBackground(new Color (29,233,182));
        addButton.setFont(new Font("Roboto Medium", Font.PLAIN, 30));
        addButton.setForeground(Color.WHITE);
        addButton.setBorderPainted(false);
        addButton.setFocusPainted(false);
        addButton.addActionListener(this);
        animate2.add(addButton);

        /* Sets buttonPanel | scrollPane | scrollBar properties */
        scrollBar.setPreferredSize(new Dimension(0, 0));
        scrollBar.setUnitIncrement(16);
        buttonPanel.setBackground(new Color (238,238,238));
        buttonPanel.setLayout(buttonLayout);
        scrollPane.setBorder(null);
        this.add(scrollPane);

        /* Sets the window visible */
        this.setVisible(true);

        /* Initializes the SoundBoard's list*/
        sound.initSoundList();
    }

    public void actionPerformed(ActionEvent arg0) {
        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){
            System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
            boolean checkMP3 = new File("sounds/", fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ".wav")).exists();
            boolean check = new File("sounds/", fc.getSelectedFile().getName()).exists();
            if (!check && !checkMP3) {
                Button soundButton = sound.addSound(fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""), fc.getSelectedFile().getAbsolutePath());
                sound.CopyBinaryFileWithStreams( fc.getSelectedFile().getAbsolutePath(), fc.getSelectedFile().getName());
                soundButton.setFilename("sounds/" + fc.getSelectedFile().getName());
                if (soundButton.readExt().equals(".mp3"))
                    sound.convertMp3toWav(soundButton);
            } else {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "The sound's file has already been added to the SoundBoard !", " Import Error ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}