package robots.src.gui;
import robots.src.log.Logger;

import java.rmi.MarshalException;
import java.util.Observable;
import java.awt.*;
import robots.src.models.Robot;
import robots.src.models.Target;


/**
 * ПерсональнаяЗадачаЛандышев
 * Класс просчёта движения робота, выделенный из исходного GameMovementModelling
 */
public class GameMovementModelling{
    private Robot playerRobot = new Robot(100,100,0,0.1,0.01);

    private Target target = new Target(150,100);

    public Robot getPlayerRobot() {
        return this.playerRobot;
    }

    public Target getTarget() {
        return target;
    }



    private double distance(double x1, double y1, double x2, double y2)
    {
        double diffX = x1 - x2;
        double diffY = y1 - y2;
        return Math.sqrt(diffX * diffX + diffY * diffY);
    }

    private double angleTo(double fromX, double fromY, double toX, double toY)
    {
        double diffX = toX - fromX;
        double diffY = toY - fromY;
        return asNormalizedRadians(Math.atan2(diffY, diffX));
    }

    public void onModelUpdateEvent() {
        double distance = distance(target.getPositionX(), target.getPositionY(), playerRobot.getPositionX(), playerRobot.getPositionY());
        if (distance < 0.5) {

            return;

        }

        double angleToTarget = angleTo(playerRobot.getPositionX(), playerRobot.getPositionY(), target.getPositionX(), target.getPositionY());

        double angleDifference = angleToTarget - playerRobot.getRobotDirection();

        if (angleDifference > Math.PI) {
            angleDifference -= 2 * Math.PI;
        } else if (angleDifference < -Math.PI) {
            angleDifference += 2 * Math.PI;
        }
        double angularVelocity;
        if (angleDifference > 0) {
            angularVelocity = Math.min(playerRobot.getMaxAngularVelocity(), angleDifference);
        } else {
            angularVelocity = Math.max(-playerRobot.getMaxAngularVelocity(), angleDifference);
        }

        moveRobot(playerRobot.getMaxVelocity(), angularVelocity, 10);
    }

        private double applyLimits(double value, double min, double max)
        {
            if (value < min)
                return min;
            if (value > max)
                return max;
            return value;
        }

        private void moveRobot(double velocity, double angularVelocity, double duration)
        {

            velocity = applyLimits(velocity, 0, playerRobot.getMaxVelocity());
            angularVelocity = applyLimits(angularVelocity, -playerRobot.getMaxAngularVelocity(), playerRobot.getMaxAngularVelocity());
            double newX = playerRobot.getPositionX() + velocity / angularVelocity *
                    (Math.sin(playerRobot.getRobotDirection()  + angularVelocity * duration) -
                            Math.sin(playerRobot.getRobotDirection()));
            double newY = playerRobot.getPositionY() - velocity / angularVelocity *
                    (Math.cos(playerRobot.getRobotDirection()  + angularVelocity * duration) -
                            Math.cos(playerRobot.getRobotDirection()));
            if (!Double.isFinite(newX)) {
                newX = playerRobot.getPositionX() + velocity * duration * Math.cos(playerRobot.getRobotDirection());
            }
            if (!Double.isFinite(newY))
            {
                newY = playerRobot.getPositionY() + velocity * duration * Math.sin(playerRobot.getRobotDirection());
            }
            playerRobot.setPositionX(newX);
            playerRobot.setPositionY(newY);

            double angle = playerRobot.getRobotDirection() + angularVelocity * duration;
            playerRobot.setRobotDirection(asNormalizedRadians(angle));

        }

        private double asNormalizedRadians(double angle) {
            while (angle < 0) {
                angle += 2 * Math.PI;
            }
            while (angle >= 2 * Math.PI) {
                angle -= 2 * Math.PI;
            }
            return angle;
        }
}
