package robots.src.models;

import java.awt.*;

/**
 * Класс, представляющий цель, к которой робот следует по механике игры
 */
public class Target extends Entity{

    public Target(int positionX, int positionY) {
        this.positionX = positionX;
        this.positionY = positionY;
    }
    public void setTargetPosition(Point p)
    {
        this.positionX = p.x;
        this.positionY = p.y;
    }

}
