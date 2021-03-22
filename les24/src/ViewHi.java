import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class ViewHi extends JFrame implements MouseListener  {

    protected JLabel helloLabel = new JLabel("Hello");
    protected JTextField userInputTextField = new JTextField(20);
    private JButton sayHiBtn = new JButton("Say Hi");

    private ControllerHi controller;

    public ViewHi(ControllerHi controller) {
        this.controller = controller;

        //... Layout the components.
        JPanel content = new JPanel();
        content.setLayout(new FlowLayout());
        content.add(new JLabel("Enter your name"));
        content.add(userInputTextField);
        content.add(sayHiBtn);
        content.add(helloLabel);

        //... finalize layout
        this.setContentPane(content);
        this.pack();
        this.setTitle("Simple App - MVC");

        // Add a mouse listener to the button
        sayHiBtn.addMouseListener(this);

        // The window closing event should probably be passed to the
        // Controller in a real program, but this is a short example.
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void updateLabel(String s) {
        helloLabel.setText(s);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        controller.setName(userInputTextField.getText());
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
}
