package robots.src.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import robots.src.log.Logger;
import java.io.*;
import java.util.Properties;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.util.HashMap;
import java.util.Map;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается. 
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 *
 */
public class MainApplicationFrame extends JFrame
{
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final Map<String, InternalFrameState> internalFrameStates = new HashMap<>();
    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width  - inset*2,
                screenSize.height - inset*2);

        setContentPane(desktopPane);


        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400,  400);
        addWindow(gameWindow);

        setJMenuBar(generateMenuBar());


        restoreWindowState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.out.println("Главное окно приложения закрывается...");
                saveWindowState();
            }
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }



    private void saveWindowState() {
        Properties properties = new Properties();
        try (FileOutputStream fos = new FileOutputStream("window_state.properties")) {
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                InternalFrameState state = new InternalFrameState(
                        internalFrame.getX(),
                        internalFrame.getY(),
                        internalFrame.getWidth(),
                        internalFrame.getHeight(),
                        internalFrame.isIcon()
                );
                internalFrameStates.put(windowId, state);
                // Сохранение состояния окна в файл свойств
                properties.setProperty(windowId + "_x", String.valueOf(state.getX()));
                properties.setProperty(windowId + "_y", String.valueOf(state.getY()));
                properties.setProperty(windowId + "_width", String.valueOf(state.getWidth()));
                properties.setProperty(windowId + "_height", String.valueOf(state.getHeight()));
                properties.setProperty(windowId + "_isIcon", String.valueOf(state.isIcon()));
            }
            properties.store(fos, "Window states");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }




    private void restoreWindowState() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("window_state.properties")) {
            properties.load(fis);
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                int x = Integer.parseInt(properties.getProperty(windowId + "_x", "0"));
                int y = Integer.parseInt(properties.getProperty(windowId + "_y", "0"));
                int width = Integer.parseInt(properties.getProperty(windowId + "_width", "0"));
                int height = Integer.parseInt(properties.getProperty(windowId + "_height", "0"));
                boolean isIcon = Boolean.parseBoolean(properties.getProperty(windowId + "_isIcon", "true"));
                InternalFrameState state = new InternalFrameState(x, y, width, height, isIcon);
                internalFrameStates.put(windowId, state);
            }
            // Применение состояния окон при восстановлении
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                InternalFrameState state = internalFrameStates.get(windowId);
                if (state != null) {
                    internalFrame.setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
                    internalFrame.setIcon(state.isIcon());
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }



    protected LogWindow createLogWindow()
    {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10,10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }
    
    protected void addWindow(JInternalFrame frame)
    {
        desktopPane.add(frame);
        frame.setVisible(true);
    }
    
//    protected JMenuBar createMenuBar() {
//        JMenuBar menuBar = new JMenuBar();
// 
//        //Set up the lone menu.
//        JMenu menu = new JMenu("Document");
//        menu.setMnemonic(KeyEvent.VK_D);
//        menuBar.add(menu);
// 
//        //Set up the first menu item.
//        JMenuItem menuItem = new JMenuItem("New");
//        menuItem.setMnemonic(KeyEvent.VK_N);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_N, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("new");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        //Set up the second menu item.
//        menuItem = new JMenuItem("Quit");
//        menuItem.setMnemonic(KeyEvent.VK_Q);
//        menuItem.setAccelerator(KeyStroke.getKeyStroke(
//                KeyEvent.VK_Q, ActionEvent.ALT_MASK));
//        menuItem.setActionCommand("quit");
////        menuItem.addActionListener(this);
//        menu.add(menuItem);
// 
//        return menuBar;
//    }
    
    private JMenuBar generateMenuBar()
    {
        JMenuBar menuBar = new JMenuBar();
        
        JMenu lookAndFeelMenu = new JMenu("Режим отображения");
        lookAndFeelMenu.setMnemonic(KeyEvent.VK_V);
        lookAndFeelMenu.getAccessibleContext().setAccessibleDescription(
                "Управление режимом отображения приложения");
        
        {
            JMenuItem systemLookAndFeel = new JMenuItem("Системная схема", KeyEvent.VK_S);
            systemLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(systemLookAndFeel);
        }

        {
            JMenuItem crossplatformLookAndFeel = new JMenuItem("Универсальная схема", KeyEvent.VK_S);
            crossplatformLookAndFeel.addActionListener((event) -> {
                setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
                this.invalidate();
            });
            lookAndFeelMenu.add(crossplatformLookAndFeel);
        }

        JMenu testMenu = new JMenu("Тесты");
        testMenu.setMnemonic(KeyEvent.VK_T);
        testMenu.getAccessibleContext().setAccessibleDescription(
                "Тестовые команды");
        
        {
            JMenuItem addLogMessageItem = new JMenuItem("Сообщение в лог", KeyEvent.VK_S);
            addLogMessageItem.addActionListener((event) -> {
                Logger.debug("Новая строка");
            });
            testMenu.add(addLogMessageItem);
        }

        menuBar.add(lookAndFeelMenu);
        menuBar.add(testMenu);
        return menuBar;
    }
    
    private void setLookAndFeel(String className)
    {
        try
        {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        }
        catch (ClassNotFoundException | InstantiationException
            | IllegalAccessException | UnsupportedLookAndFeelException e)
        {
            // just ignore
        }
    }
}
