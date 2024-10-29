package robots.src.gui.Windows;

import robots.src.gui.GameVisualizer;
import robots.src.gui.RobotPropertiesUploaders.RobotPropertiesWriter;
import robots.src.gui.RobotPropertiesUploaders.RobotPropertiesReader;
import robots.src.models.Robot;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class ClassLoaderRobotWindow extends JInternalFrame implements ActionListener {
    private JButton loadButton;
    private JButton uploadButton;

    private GameWindow gameWindow;

    public ClassLoaderRobotWindow(GameWindow gameWindow) {
        super("Class Loader Uploader", true, true, true, true);
        setSize(300, 200);
        setLayout(new FlowLayout());

        loadButton = new JButton("Загрузить");
        uploadButton = new JButton("Выгрузить");

        loadButton.addActionListener(this);
        uploadButton.addActionListener(this);

        add(loadButton);
        add(uploadButton);

        this.gameWindow = gameWindow;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loadButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выберите .jar файл");
            fileChooser.setFileFilter(new FileNameExtensionFilter(".jar files", "jar"));

            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                String filePath = selectedFile.getAbsolutePath();

                // Now call your loadClassesFromJar method with the selected file path
                gameWindow.setVisualizer(RobotPropertiesReader.loadClassesFromJar(filePath));
            }
        } else if (e.getSource() == uploadButton) {
            Robot robot = gameWindow.getVisualizer().getGameMovementModelling().getPlayerRobot();

            // Открываем диалоговое окно выбора файла
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Выберите место сохранения и имя файла");

            // Устанавливаем фильтр для JAR-файлов (необязательно)
            fileChooser.setFileFilter(new FileNameExtensionFilter("JAR files (*.jar)", "jar"));

            // Открываем диалоговое окно выбора файла
            int userSelection = fileChooser.showSaveDialog(null);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                // Получаем выбранный файл
                File selectedFile = fileChooser.getSelectedFile();

                // Получаем путь и имя файла
                String filePath = selectedFile.getAbsolutePath();

                // Добавляем расширение .jar, если его нет
                if (!filePath.toLowerCase().endsWith(".jar")) {
                    filePath += ".jar";
                }

                // Сохраняем JAR-файл с выбранным путем и именем
                RobotPropertiesWriter.writeRobotPropertiesToFile(robot, filePath);
            }
        }
    }
}