package robots.src.gui;
import java.io.Serializable;
import java.util.Locale;

/**
 * ПерсональнаяЗадачаКоптелов
 * Описывает состояние внутреннего окна
 */
public class InternalFrameState implements Serializable {
    private static final long serialVersionUID = 1L;
    private Locale frameLanguage;
    private int x;
    private int y;
    private int width;
    private int height;
    private boolean isIcon;

    /**
     * ПерсональнаяЗадачаКоптелов
     * Конструктор класса InternalFrameState.
     * @param x координата X окна
     * @param y координата Y окна
     * @param frameLanguage язык окна
     * @param width ширина окна
     * @param height высота окна
     * @param isIcon флаг иконизированного состояния окна
     */
    public InternalFrameState(int x, int y, Locale frameLanguage, int width, int height, boolean isIcon) {
        this.x = x;
        this.y = y;
        this.frameLanguage = frameLanguage;
        this.width = width;
        this.height = height;
        this.isIcon = isIcon;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Locale getFrameLanguage() {
        return frameLanguage;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isIcon() {
        return isIcon;
    }
}