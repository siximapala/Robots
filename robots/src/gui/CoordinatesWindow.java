package robots.src.gui;

import robots.src.models.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class CoordinatesWindow extends JInternalFrame implements Observer
{
    private JLabel label;

    public CoordinatesWindow(GameWindow gameWindow)
    {
        super("Координаты робота", true, true, true, true);
        label = new JLabel("Координата X: 0, Координата Y: 0");
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();

        gameWindow.getVisualizer().getGameMovementModelling().getPlayerRobot().addObserver(this);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Entity subject) {
            label.setText("Координата X: " + subject.getPositionX() + ", Координата Y: " + subject.getPositionY());
        }
    }
}
