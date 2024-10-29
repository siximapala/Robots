package robots.src.gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.swing.*;

import robots.src.gui.Windows.ClassLoaderRobotWindow;
import robots.src.gui.Windows.CoordinatesWindow;
import robots.src.gui.Windows.GameWindow;
import robots.src.gui.Windows.LogWindow;
import robots.src.log.Logger;

import static javax.swing.UIManager.setLookAndFeel;


public class MainApplicationFrame extends JFrame {
    private static ResourceBundle defaultBundle = ResourceBundle.getBundle("robots.resources.lang");

    private final CoordinatesWindow coordinatesWindow;

    private final ClassLoaderRobotWindow classLoaderUploaderWindow;
    private TestsMenu testsMenu;
    private LookAndFeelMenu lookAndFeelMenu;
    private LanguageMenu languageMenu;


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
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

        this.coordinatesWindow = new CoordinatesWindow(gameWindow);
        coordinatesWindow.setSize(400, 400);
        addWindow(coordinatesWindow);

        this.classLoaderUploaderWindow = new ClassLoaderRobotWindow(gameWindow);
        classLoaderUploaderWindow.setSize(400,100);
        addWindow(classLoaderUploaderWindow);

        setJMenuBar(generateMenuBar());


        restoreWindowState();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveWindowState();
            }
        });


        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    /**
     * ПерсональнаяЗадачаКоптелов
     * Сохраняет состояния внутренних окон в файл свойств.
     */
    public void saveWindowState() {
        Properties properties = new Properties();
        try (FileOutputStream fos = new FileOutputStream("robots/resources/window_state.properties")) {
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                InternalFrameState state = new InternalFrameState(
                        internalFrame.getX(),
                        internalFrame.getY(),
                        getLocale(),
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
            properties.setProperty("languageGlobal", String.valueOf(Locale.getDefault()));
            properties.store(fos, "Window states");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }



    /**
     * ПерсональнаяЗадачаКоптелов
     * Метод для восстановления состояния окон приложения из файла настроек.
     * Восстанавливает размер, положение и состояние иконки окон.
     */
    private void restoreWindowState() {
        Properties properties = new Properties();
        try (FileInputStream fis = new FileInputStream("robots/resources/window_state.properties")) {
            properties.load(fis);
            languageMenu.setLanguage(Locale.forLanguageTag(properties.getProperty("languageGlobal")));
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                int x = Integer.parseInt(properties.getProperty(windowId + "_x", "0"));
                int y = Integer.parseInt(properties.getProperty(windowId + "_y", "0"));
                int width = Integer.parseInt(properties.getProperty(windowId + "_width", "0"));
                int height = Integer.parseInt(properties.getProperty(windowId + "_height", "0"));
                boolean isIcon = Boolean.parseBoolean(properties.getProperty(windowId + "_isIcon", "true"));
                InternalFrameState state = new InternalFrameState(x, y, Locale.getDefault(),width, height, isIcon);
                internalFrameStates.put(windowId, state);
            }
            // Применение состояния окон при восстановлении
            for (JInternalFrame internalFrame : desktopPane.getAllFrames()) {
                String windowId = internalFrame.getTitle();
                InternalFrameState state = internalFrameStates.get(windowId);
                if (state != null) {
                    internalFrame.setBounds(state.getX(), state.getY(), state.getWidth(), state.getHeight());
                    internalFrame.setLocale(state.getFrameLanguage());
                    internalFrame.setIcon(state.isIcon());
                }
            }
        } catch (IOException | NumberFormatException ex) {
            ex.printStackTrace();
        } catch (PropertyVetoException e) {
            throw new RuntimeException(e);
        }
    }


    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    /**
     * ПерсональнаяЗадачаЛандышев
     * Возвращает окно координат робота.
     * @return объект типа CoordinatesWindow, представляющий окно координат робота
     */
    public CoordinatesWindow getCoordinatesWindow() {
        return coordinatesWindow;
    }

    /**
     * ГрупповаяЗадача-1
     * Был изменён процесс генерации бара меню. Теперь главные элементы меню выделены по классам,
     * экземпляры которых добавляются в меню бар.
     */
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        testsMenu = new TestsMenu(MainApplicationFrame.getResourceBundle().getString("TestsMenuBar"));
        menuBar.add(testsMenu);

        lookAndFeelMenu = new LookAndFeelMenu(MainApplicationFrame.getResourceBundle().getString("LookAndFeelMenuBar"));
        menuBar.add(lookAndFeelMenu);

        languageMenu = new LanguageMenu(MainApplicationFrame.getResourceBundle().getString("LanguageMenuBar"));
        menuBar.add(languageMenu);

        return menuBar;
    }

    /**
     * ГрупповаяЗадача-1
     * Меню с тестовыми командами
     */
    private class TestsMenu extends JMenu {

        private JMenuItem addLogMessageItem = new JMenuItem(MainApplicationFrame.getResourceBundle().getString("MenuTests_logMessage"), KeyEvent.VK_S);

        /**
         * ГрупповаяЗадача-1
         * Создает TestsMenu с указанным заголовком
         * @param title заголовок меню
         */
        public TestsMenu(String title) {
            super(title);
            setMnemonic(KeyEvent.VK_T);
            getAccessibleContext().setAccessibleDescription("Тестовые команды");

            addLogMessageItem.addActionListener(e -> Logger.debug(MainApplicationFrame.getResourceBundle().getString("TestsDebugMessage")));
            add(addLogMessageItem);
        }

        /**
         * ГрупповаяЗадача-1
         * Обновляет текст пункта меню для добавления сообщения в лог в соответствии с локализованными ресурсам
         */
        public void updateAddLogMessageItemButtonText(){
            addLogMessageItem.setText(MainApplicationFrame.getResourceBundle().getString("MenuTests_logMessage"));
        }
    }


    /**
     * ГрупповаяЗадача-1
     * Представляет меню с опциями для изменения внешнего вида приложения
     */
    public class LookAndFeelMenu extends JMenu {

        private JMenuItem systemLookAndFeel = new JMenuItem(MainApplicationFrame.getResourceBundle().getString("LookAndFeelMenuBar_option1"));

        private JMenuItem crossplatformLookAndFeel = new JMenuItem(MainApplicationFrame.getResourceBundle().getString("LookAndFeelMenuBar_option2"));

        /**
         * ГрупповаяЗадача-1
         * Создает LookAndFeelMenu с указанным заголовком
         * @param title заголовок меню
         */
        public LookAndFeelMenu(String title) {
            super(title);
            systemLookAndFeel.addActionListener(e -> setLookAndFeel(UIManager.getSystemLookAndFeelClassName()));
            this.add(systemLookAndFeel);

            crossplatformLookAndFeel.addActionListener(e -> setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()));
            this.add(crossplatformLookAndFeel);
        }

        /**
         * ГрупповаяЗадача-1
         * Обновляет текст пункта меню системного вида в соответствии с локализованными ресурсами
         */
        public void updateSystemLookAndFeelButtonText(){
            systemLookAndFeel.setText(MainApplicationFrame.getResourceBundle().getString("LookAndFeelMenuBar_option1"));
        }

        /**
         * ГрупповаяЗадача-1
         * Обновляет текст пункта меню кросс-платформенного вида в соответствии с локализованными ресурсами
         */
        public void updateCrossplatformLookAndFeelButtonText(){
            crossplatformLookAndFeel.setText(MainApplicationFrame.getResourceBundle().getString("LookAndFeelMenuBar_option2"));
        }

        /**
         * ГрупповаяЗадача-1
         * Устанавливает указанный внешний вид для приложения.
         * @param className полное имя класса внешнего вида, который необходимо установить
         */
        private void setLookAndFeel(String className) {
            try {
                UIManager.setLookAndFeel(className);
                SwingUtilities.updateComponentTreeUI(SwingUtilities.getRoot(LookAndFeelMenu.this));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


    /**
     * ГрупповаяЗадача-1
     * Класс, представляющий меню выбора языка.
     * Содержит пункты меню для выбора английского, немецкого и русского языков.
     */
    private class LanguageMenu extends JMenu {

        /**
         * Создает новый экземпляр меню выбора языка с указанным заголовком.
         * @param title заголовок меню
         */
        public LanguageMenu(String title) {
            super(title);

            JMenuItem englishLanguageItem = new JMenuItem("English");
            englishLanguageItem.addActionListener(e -> setLanguage(new Locale("en")));
            add(englishLanguageItem);

            JMenuItem germanLanguageItem = new JMenuItem("Deutsch");
            germanLanguageItem.addActionListener(e -> setLanguage(new Locale("de")));
            add(germanLanguageItem);

            JMenuItem russianLanguageItem = new JMenuItem("Русский");
            russianLanguageItem.addActionListener(e -> setLanguage(new Locale("ru")));
            add(russianLanguageItem);
        }

        /**
         * ГрупповаяЗадача-1
         * Устанавливает указанный язык для приложения.
         * @param locale объект Locale, представляющий выбранный язык
         */
        private void setLanguage(Locale locale) {
            Locale.setDefault(locale);
            defaultBundle = ResourceBundle.getBundle("robots.resources.lang");
            updateInterfaceLanguage();
            updateInterfaceLanguageManualParts();
        }
    }

    /**
     * ГрупповаяЗадача-1
     * Возвращает ресурсный пакет приложения.
     * @return объект типа ResourceBundle, представляющий ресурсы приложения
     */
    public static ResourceBundle getResourceBundle() {
        return defaultBundle;
    }

    /**
     * ГрупповаяЗадача-1
     * Возвращает панель рабочего стола приложения.
     * @return объект типа JDesktopPane, представляющий панель рабочего стола
     */
    private JDesktopPane getDesktopPane() {
        return this.desktopPane;
    }


    /**
     * ГрупповаяЗадача-1
     * Обновляет язык интерфейса приложения.
     * Изменяет названия внутренних фреймов и пунктов меню согласно выбранному языку.
     */
    private void updateInterfaceLanguage() {

        JInternalFrame[] frames = getDesktopPane().getAllFrames();
        for (JInternalFrame frame : frames) {
            Class<?> clazz = frame.getClass();
            String className = clazz.getSimpleName() + "FrameText";
            if (getResourceBundle().containsKey(className)) {
                frame.setTitle(getResourceBundle().getString(className));
            }
        }

        JMenuBar menuBar = getDesktopPane().getRootPane().getJMenuBar();
        int menuCount = menuBar.getMenuCount();
        JMenu[] menus = new JMenu[menuCount];
        for (int i = 0; i < menuCount; i++) {
            menus[i] = menuBar.getMenu(i);
        }
        for (JMenu menu : menus) {
            String className = menu.getClass().getSimpleName() + "Bar";
            if (MainApplicationFrame.getResourceBundle().containsKey(className)) {
                menu.setText(MainApplicationFrame.getResourceBundle().getString(className));
            }
        }

    }

    /**
     * ГрупповаяЗадача-1
     * Обновляет части интерфейса, требующие ручного обновления после смены языка.
     * Включает в себя обновление названий кнопок и текстовых элементов.
     */
    private void updateInterfaceLanguageManualParts(){
        lookAndFeelMenu.updateSystemLookAndFeelButtonText();
        lookAndFeelMenu.updateCrossplatformLookAndFeelButtonText();
        testsMenu.updateAddLogMessageItemButtonText();
        coordinatesWindow.updateLabelText();
    }

}


