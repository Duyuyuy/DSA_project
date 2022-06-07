import java.awt.*;

public class Marker extends Cell {
    private final Color HIT_COLOUR = new Color(255, 0, 0, 180);
    private final Color MISS_COLOUR = new Color(0, 0, 255, 180);
    private final int PADDING = 3;
    private boolean showMarker;
    private Ship shipAtMarker;
    
    public Marker(int x, int y, int width, int height) {
        super(x, y, width, height);
        reset();
    }

    public void reset() {
        shipAtMarker = null;
        showMarker = false;
    }

    public void mark() {
        if (!showMarker && isShip()) {
            shipAtMarker.destroySection();
        }
        showMarker = true;
    }

    public boolean isMarked() {
        return showMarker;
    }

    public void setAsShip(Ship ship) {
        this.shipAtMarker = ship;
    }

    public boolean isShip() {
        return shipAtMarker != null;
    }

    public Ship getShip() {
        return shipAtMarker;
    }

    public void paint(Graphics g) {
        if (!showMarker) return;

        g.setColor(isShip() ? HIT_COLOUR : MISS_COLOUR);
        g.fillRect(position.x + PADDING, position.y + PADDING, width - PADDING * 2, height - PADDING * 2);
    }
}
