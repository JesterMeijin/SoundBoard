import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class SearchEngine extends JTextField implements DocumentListener, FocusListener {

    private SoundManager soundManager;
    private final JPanel buttonPanel;

    SearchEngine(String text, JPanel buttonPanel, SoundManager soundManager){
        super(text);
        this.soundManager = soundManager;
        this.buttonPanel = buttonPanel;
        this.setBorder(null);
        this.setBackground(new Color (29,233,182));
        this.setFont(new Font("Roboto Medium",Font.PLAIN, 25));
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setForeground(Color.WHITE);
        this.addFocusListener(this);
        this.getDocument().addDocumentListener(this);
    }

    @Override
    public void focusGained(FocusEvent e){
        this.setText("");
    }

    @Override
    public void focusLost(FocusEvent e){
        if (this.getText().equals(""))
            this.setText("Search");
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    private void dynamicSearch(String searchEntry){
        for (SoundButton soundButton : soundManager.getSoundButtons()) {
            buttonPanel.add(soundButton);
            buttonPanel.revalidate();
            if (!soundButton.getText().toLowerCase().contains(searchEntry.toLowerCase()) && !searchEntry.equals("Search"))
                buttonPanel.remove(soundButton);

        }
        buttonPanel.repaint();
    }
}
