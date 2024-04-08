package robots.src.gui;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private final GameVisualizer visualizer;
    private String windowTitle = MainApplicationFrame.getResourceBundle().getString("GameWindowFrameText");

    public GameWindow()
    {
        super(MainApplicationFrame.getResourceBundle().getString("GameWindowFrameText"), true, true, true, true);
        visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public GameVisualizer getVisualizer() {
        return visualizer;
    }
}
