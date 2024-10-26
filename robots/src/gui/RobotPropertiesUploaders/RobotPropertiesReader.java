package robots.src.gui.RobotPropertiesUploaders;


import robots.src.gui.GameMovementModelling;
import robots.src.gui.GameVisualizer;
import robots.src.gui.MainApplicationFrame;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class RobotPropertiesReader {

    public static GameVisualizer getGameVisualizerInstance() {
        return gameVisualizerInstance;
    }

    private static GameVisualizer gameVisualizerInstance;

    public static GameVisualizer loadClassesFromJar(String jarFilePath) {
        try {
            // Проверяем, что файл существует и имеет расширение .jar
            File file = new File(jarFilePath);
            if (!file.exists() || !file.isFile() || !jarFilePath.toLowerCase().endsWith(".jar")) {
                System.out.println("Указанный файл не является действительным .jar архивом.");
                return null;
            }

            // Создаем URLClassLoader для загрузки классов из .jar файла
            URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{file.toURI().toURL()});

            Class<?> gameMovementModellingInstance = null;

            // Получаем список всех классов в .jar файле
            try (JarFile jarFile = new JarFile(file)) {
                for (Enumeration<JarEntry> entries = jarFile.entries(); entries.hasMoreElements();) {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class")) {
                        String className = entry.getName().replace("/", ".").replace(".class", "");
                        Class<?> loadedClass = classLoader.loadClass(className);

                        if (loadedClass.getName().equals("robots.src.gui.GameMovementModelling")) {
                            // Create an instance of GameMovementModelling
                            gameMovementModellingInstance = (Class<?>) loadedClass.getDeclaredConstructor().newInstance();
                            System.out.println(gameMovementModellingInstance.get);
                        }
                    }
                }

                // Now that wwe have the instance of GameMovementModelling, let's proceed to load GameVisualizer
                if (gameMovementModellingInstance != null) {
                    Class<?> visualizerClass = classLoader.loadClass("robots.src.gui.GameVisualizer");
                    // Check if GameVisualizer class requires GameMovementModelling in its constructor
                    Constructor<?>[] constructors = visualizerClass.getConstructors();
                    for (Constructor<?> constructor : constructors) {
                        System.out.println(constructor);
                        if (constructor.getParameterCount() == 1 && constructor.getParameterTypes()[0].getName().equals("robots.src.gui.GameMovementModelling")) {
                            // Create an instance of GameVisualizer using the constructor that takes GameMovementModelling as parameter
                            gameVisualizerInstance = (GameVisualizer) constructor.newInstance(gameMovementModellingInstance);
                            // Now you have instances of both GameMovementModelling and GameVisualizer to work with
                            return gameVisualizerInstance;
                        }
                    }
                }
            }

            classLoader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    return null;
    }



}
