package robots.src.models;

import java.awt.*;

/**
 * Класс, представляющий робота для поля игры.
 */
public class Robot extends Entity{

    public Robot(int positionX, int positionY, double robotDirection, double maxVelocity, double maxAngularVelocity) {
        this.positionX = positionX;
        this.positionY = positionY;
        this.robotDirection = robotDirection;
        this.maxVelocity = maxVelocity;
        this.maxAngularVelocity = maxAngularVelocity;
    }
    private volatile double robotDirection = 0;
    private double maxVelocity = 0.1;
    private double maxAngularVelocity = 0.01;


    public void setRobotPosition(Point p){
        this.positionX = p.x;
        this.positionY = p.y;
    }


    public double getRobotDirection(){
        return this.robotDirection;
    }

    public double getMaxVelocity() {
        return this.maxVelocity;
    }

    public double getMaxAngularVelocity() {
        return this.maxAngularVelocity;
    }

    public void setMaxVelocity(double maxVelocity) {
        this.maxVelocity = maxVelocity;
    }

    public void setMaxAngularVelocity(double maxAngularVelocity) {
        this.maxAngularVelocity = maxAngularVelocity;
    }

    public void setRobotDirection(double robotDirection) {
        this.robotDirection = robotDirection;
    }

}
