package robots.src.gui.Windows;

import robots.src.gui.GameVisualizer;
import robots.src.gui.MainApplicationFrame;

import java.awt.BorderLayout;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame
{
    private GameVisualizer gameVisualizer;
    private String windowTitle = MainApplicationFrame.getResourceBundle().getString("GameWindowFrameText");

    public GameWindow()
    {
        super(MainApplicationFrame.getResourceBundle().getString("GameWindowFrameText"), true, true, true, true);
        gameVisualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(gameVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }

    public GameVisualizer getVisualizer() {
        return gameVisualizer;
    }

    public void setVisualizer(GameVisualizer gameVisualizer){
        this.gameVisualizer = gameVisualizer;
    }

}
