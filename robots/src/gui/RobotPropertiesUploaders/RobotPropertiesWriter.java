package robots.src.gui.RobotPropertiesUploaders;

import org.objectweb.asm.*;
import robots.src.gui.GameMovementModelling;
import robots.src.gui.GameVisualizer;
import robots.src.models.Robot;

import java.io.*;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import java.io.IOException;
import java.io.InputStream;


public class RobotPropertiesWriter {
    public static void writeRobotPropertiesToFile(Robot robot, String outputPath) {
        try {

            // Создаем временный JAR-файл
            File tempJarFile = File.createTempFile("temp", ".jar");
            JarOutputStream tempJarOutputStream = new JarOutputStream(new FileOutputStream(tempJarFile), new Manifest());

            // Добавляем MainMovementModelling и GameVisualizer во временный JAR
            addModifiedClassToJar(GameMovementModelling.class, robot, tempJarOutputStream);
            addClassToJar(GameVisualizer.class, tempJarOutputStream);

            tempJarOutputStream.close();

            // Перемещаем временный JAR в выходной JAR
            File outputFile = new File(outputPath);
            tempJarFile.renameTo(outputFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addModifiedClassToJar(Class<?> clazz, Robot robot, JarOutputStream jarOutputStream) throws IOException {
        String className = clazz.getName().replace('.', '/') + ".class";
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(className);
        if (inputStream == null) {
            throw new IOException("Class file not found for: " + className);
        }

        ClassReader classReader = new ClassReader(inputStream);
        ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_FRAMES);
        ClassVisitor classVisitor = new ClassVisitor(Opcodes.ASM7, classWriter) {
            private boolean flag = true;

            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                return new MethodVisitor(Opcodes.ASM7, mv) {
                    @Override
                    public void visitInsn(int opcode) {
                        if (opcode == Opcodes.RETURN && flag) {
                            mv.visitVarInsn(Opcodes.ALOAD, 0);
                            mv.visitTypeInsn(Opcodes.NEW, Type.getInternalName(Robot.class)); // Используем Type.getInternalName для получения внутреннего имени класса
                            mv.visitInsn(Opcodes.DUP);
                            mv.visitLdcInsn(robot.getPositionX());
                            mv.visitLdcInsn(robot.getPositionY());
                            mv.visitLdcInsn(robot.getRobotDirection());
                            mv.visitLdcInsn(robot.getMaxVelocity());
                            mv.visitLdcInsn(robot.getMaxAngularVelocity());
                            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Type.getInternalName(Robot.class), "<init>", "(DDDDD)V", false);
                            mv.visitFieldInsn(Opcodes.PUTFIELD, clazz.getName().replace('.', '/'), "playerRobot", Type.getDescriptor(Robot.class)); // Используем Type.getDescriptor для получения дескриптора класса
                            flag = false;
                        }
                        super.visitInsn(opcode);
                    }
                };
            }
        };
        classReader.accept(classVisitor, ClassReader.EXPAND_FRAMES);

        jarOutputStream.putNextEntry(new JarEntry(className));
        jarOutputStream.write(classWriter.toByteArray());
        jarOutputStream.closeEntry();

        inputStream.close();
    }


    private static void addClassToJar(Class<?> clazz, JarOutputStream jarOutputStream) throws IOException {
        String className = clazz.getName().replace('.', '/') + ".class";
        InputStream inputStream = clazz.getClassLoader().getResourceAsStream(className);
        if (inputStream == null) {
            throw new IOException("Class file not found for: " + className);
        }

        jarOutputStream.putNextEntry(new JarEntry(className));
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            jarOutputStream.write(buffer, 0, bytesRead);
        }
        jarOutputStream.closeEntry();
        inputStream.close();
    }
}