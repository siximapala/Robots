package robots.src.models;

import java.util.Observable;

/** Абстрактный класс, представляющий объект на поле игры.
*/
public abstract class Entity extends Observable {

    protected volatile double positionX;
    protected volatile double positionY;
    public double getPositionX() {
        return positionX;
    }

    public double getPositionY() {
        return positionY;
    }

    public void setPositionX(double positionX) {
        this.positionX = positionX;
        setChanged();
        notifyObservers();
    }

    public void setPositionY(double positionY) {
        this.positionY = positionY;
        setChanged();
        notifyObservers();
    }



}
