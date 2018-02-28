import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

class Search extends JTextField implements DocumentListener, FocusListener {

    private Component[] components;
    private final JPanel buttonPanel;

    public Search(String text, JPanel buttonPanel){
        super(text);
        this.buttonPanel = buttonPanel;
        this.setBorder(null);
        this.setBackground(new Color (29,233,182));
        this.setFont(new Font("Roboto Medium",Font.PLAIN, 20));
        this.setHorizontalAlignment(JTextField.CENTER);
        this.setForeground(Color.WHITE);
        this.addFocusListener(this);
        this.getDocument().addDocumentListener(this);
    }

    public void setComponents(Component[] newComponents){
        components = newComponents;
    }

    public void focusGained(FocusEvent e){
        this.setText("");
    }

    public void focusLost(FocusEvent e){
        if (this.getText().equals(""))
            this.setText("Search");
    }

    public void changedUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    public void removeUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    public void insertUpdate(DocumentEvent e) {
        dynamicSearch(this.getText());
    }

    private void dynamicSearch(String searchEntry){
        for (Component comp : components) {
            if (comp instanceof Button) {
                Button button = (Button) comp;
                buttonPanel.add(button);
                buttonPanel.revalidate();
                if (!button.getText().toLowerCase().contains(searchEntry.toLowerCase()) && !searchEntry.equals("Search"))
                    buttonPanel.remove(button);
            }
        }
        buttonPanel.repaint();
    }
}
