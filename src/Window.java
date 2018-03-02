import mdlaf.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.*;
import java.io.*;


class Window extends JFrame implements ActionListener {

    private SoundManager soundManager;
    private final JFileChooser fc;
    private final FileNameExtensionFilter filter;

    Window(){

        /* Initializes variables */
        JPanel topPanel = new JPanel();
        JPanel buttonPanel = new JPanel();
        JScrollPane scrollPane = new JScrollPane(buttonPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        GridBagLayout mainLayout = new GridBagLayout();
        GridBagLayout topLayout = new GridBagLayout();
        GridLayout buttonLayout = new GridLayout(0,1);
        GridBagConstraints layoutConstraints = new GridBagConstraints();
        GridBagConstraints topLayoutConstraints = new GridBagConstraints();
        JButton addButton = new JButton("+");
        JLabel searchIcon = new JLabel(new ImageIcon(getClass().getResource("search.png")));
        JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        MaterialUIMovement animate = new MaterialUIMovement (new Color (224,224,224), 5, 1000 / 30);
        MaterialUIMovement animate2 = new MaterialUIMovement (new Color (0,191,165), 5, 1000 / 30);

        fc = new JFileChooser();
        filter = new FileNameExtensionFilter("Audio files", "wav", "aiff", "au", "mid", "midi", "mp3");
        soundManager = new SoundManager(buttonPanel, animate);

        SearchEngine search = new SearchEngine(buttonPanel, soundManager);

        /* Sets Window properties */
        setTitle("SoundBoard");
        setSize(350, 500);
        setLayout(mainLayout);
        setLocationRelativeTo(null);
        setBackground(new Color (238,238,238));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
        topPanel.add(searchIcon);
        topPanel.add(search);
        topPanel.add(addButton);
        add(topPanel);

        /* Sets searchIcon properties */
        searchIcon.setBorder(new EmptyBorder(0, 10, 0, 10));

        /* Sets addButton properties */
        addButton.setBackground(new Color (29,233,182));
        addButton.setFont(new Font("Roboto Medium", Font.PLAIN, 40));
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
        add(scrollPane);

        /* Sets the window visible */
        setVisible(true);

        /* Initializes the SoundBoard's list*/
        soundManager.initSoundList();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

        fc.setFileFilter(filter);
        int returnVal = fc.showOpenDialog(this);

        if(returnVal == JFileChooser.APPROVE_OPTION){

            System.out.println("You chose to open this file: " + fc.getSelectedFile().getName());
            boolean checkMP3 = new File("sounds/", fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ".wav")).exists();
            boolean check = new File("sounds/", fc.getSelectedFile().getName()).exists();

            if (!check && !checkMP3) {
                SoundButton soundButton = soundManager.addSound(fc.getSelectedFile().getName().replaceFirst("[.][^.]+$", ""), fc.getSelectedFile().getAbsolutePath());
                soundManager.CopyBinaryFileWithStreams( fc.getSelectedFile().getAbsolutePath(), fc.getSelectedFile().getName());
                soundButton.setFilename("sounds/" + fc.getSelectedFile().getName());
                if (soundButton.readExt().equals(".mp3"))
                    soundManager.convertMp3toWav(soundButton);
            } else {
                JFrame frame = new JFrame();
                JOptionPane.showMessageDialog(frame, "The soundManager's file has already been added to the SoundBoard !", " Import Error ", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}