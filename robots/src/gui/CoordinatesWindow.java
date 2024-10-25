package robots.src.gui;

import robots.src.models.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.text.MessageFormat;

/**
 * ПерсональнаяЗадачаЛандышев
 * Класс внутреннего окна, отображающего координаты объекта в игре.
 */
public class CoordinatesWindow extends JInternalFrame implements Observer
{
    private JLabel label;

    private double posX;
    private double posY;

    /**
     * ПерсональнаяЗадачаЛандышев
     * Конструктор класса CoordinatesWindow.
     * @param gameWindow окно игры, к которому привязывается окно координат
     */
    public CoordinatesWindow(GameWindow gameWindow)
    {
        super(MainApplicationFrame.getResourceBundle().getString("CoordinatesWindowFrameText"), true, true, true, true);
        label = new JLabel(MainApplicationFrame.getResourceBundle().getString("CoordinatesWindowPositionsText"));
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(label, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
        gameWindow.getVisualizer().getGameMovementModelling().getPlayerRobot().addObserver(this);
    }

    /**
     * ПерсональнаяЗадачаЛандышев
     * Метод, вызываемый при обновлении состояния объекта, за которым наблюдает окно координат.
     * @param o объект, за которым ведется наблюдение
     * @param arg аргумент обновления (не используется)
     */
    @Override
    public void update(Observable o, Object arg) {
        if (o instanceof Entity subject) {
            posX = subject.getPositionX();
            posY = subject.getPositionY();
            label.setText(MessageFormat.format(MainApplicationFrame.getResourceBundle().getString("CoordinatesWindowPositionsText"), posX,posY));
        }
    }
    /**
     * ГрупповаяЗадача-1
     * Метод, обновляющий заголовок окна
     * @param title строка, на которую обновляется заголовок окна
     */
    public void setTitle(String title) {
        super.setTitle(title);
    }

    /**
     * ГрупповаяЗадача-1
     * Метод, обновляющий текст внутри окна координат робота
     */
    public void updateLabelText() {
        label.setText(MessageFormat.format(MainApplicationFrame.getResourceBundle().getString("CoordinatesWindowPositionsText"), posX, posY));
    }

}
