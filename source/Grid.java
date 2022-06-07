import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid extends Cell {
    public static int SIZE = 30;
    public static int FIELD_WIDTH = 15;
    public static int FIELD_HEIGHT = 15;
    public static int[] BOAT_SIZES = {5, 4, 4, 3, 3};
    private Marker[][] markers = new Marker[FIELD_WIDTH][FIELD_HEIGHT];
    private List<Ship> ships;
    private Random rand;
    private boolean showShips;
    private boolean allShipsDestroyed;

    public Grid(int x, int y) {
        super(x, y, SIZE * FIELD_WIDTH, SIZE * FIELD_HEIGHT);
        createMarkerField();
        ships = new ArrayList<>();
        rand = new Random();
        showShips = false;
    }

    public void paint(Graphics g) {
        for (Ship ship : ships) {
            if (showShips || ship.isDestroyed()) {
                ship.paint(g);
            }
        }
        drawMarkers(g);
        drawField(g);
    }

    public void setShowShips(boolean showShips) {
        this.showShips = showShips;
    }

    public void reset() {
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                markers[x][y].reset();
            }
        }
        ships.clear();
        showShips = false;
        allShipsDestroyed = false;
    }

    public boolean markPosition(Position posToMark) {
        markers[posToMark.x][posToMark.y].mark();
        

        allShipsDestroyed = true;
        for (Ship ship : ships) {
            if (!ship.isDestroyed()) {
                allShipsDestroyed = false;
                break;
            }
        }
        return markers[posToMark.x][posToMark.y].isShip();
    }

    public boolean areAllShipsDestroyed() {
        return allShipsDestroyed;
    }

    public boolean isPositionMarked(Position posToTest) {
        return markers[posToTest.x][posToTest.y].isMarked();
    }

    public Marker getMarkerAt(Position posToSelect) {
        return markers[posToSelect.x][posToSelect.y];
    }

    public Position getPositionInField(int mouseX, int mouseY) {
        if (!isPositionInside(new Position(mouseX, mouseY))) {
        	return new Position(-1, -1);
        }

        return new Position((mouseX - position.x) / SIZE, (mouseY - position.y) / SIZE);
    }

    public boolean canPlaceShipAt(int fieldX, int fieldY, int length, boolean isHorizontal) {
        if (fieldX < 0 || fieldY < 0) return false;

        if (isHorizontal) {
            if (fieldY > FIELD_HEIGHT || fieldX + length > FIELD_WIDTH) {
            	return false;
            }
            for (int x = 0; x < length; x++) {
                if (markers[fieldX+x][fieldY].isShip()) {
                	return false;
                }
            }
        } else {
            if (fieldY + length > FIELD_HEIGHT || fieldX > FIELD_WIDTH) {
            	return false;
            }
            for (int y = 0; y < length; y++) {
                if (markers[fieldX][fieldY+y].isShip()) {
                	return false;
                }
            }
        }
        return true;
    }

    private void drawField(Graphics g) {
        g.setColor(Color.BLACK);

        int y2 = position.y;
        int y1 = position.y + height;
        for (int x = 0; x <= FIELD_WIDTH; x++)
            g.drawLine(position.x + x * SIZE, y1, position.x + x * SIZE, y2);

        int x2 = position.x;
        int x1 = position.x + width;
        for (int y = 0; y <= FIELD_HEIGHT; y++)
            g.drawLine(x1, position.y + y * SIZE, x2, position.y + y * SIZE);
    }

    private void drawMarkers(Graphics g) {
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                markers[x][y].paint(g);
            }
        }
    }

    private void createMarkerField() {
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_HEIGHT; y++) {
                markers[x][y] = new Marker(position.x + x * SIZE, position.y + y * SIZE, SIZE, SIZE);
            }
        }
    }

    public void generateShips() {
        ships.clear();
        for (int i = 0; i < BOAT_SIZES.length; i++) {
            boolean isHorizontal = rand.nextBoolean();
            int fieldX;
            int fieldY;
            
            do {
                fieldX = rand.nextInt(isHorizontal ? FIELD_WIDTH - BOAT_SIZES[i] : FIELD_WIDTH);
                fieldY = rand.nextInt(isHorizontal ? FIELD_HEIGHT : FIELD_HEIGHT - BOAT_SIZES[i]);
            } while (!canPlaceShipAt(fieldX, fieldY, BOAT_SIZES[i], isHorizontal));
            placeShip(fieldX, fieldY, BOAT_SIZES[i], isHorizontal);
        }
    }

    public void placeShip(int fieldX, int fieldY, int length, boolean sideways) {
        placeShip(new Ship(new Position(fieldX, fieldY), new Position(position.x + fieldX * SIZE, position.y + fieldY * SIZE), length, sideways), fieldX, fieldY);
    }

    public void placeShip(Ship ship, int fieldX, int fieldY) {
        ships.add(ship);
        if (ship.isHorizontal()) {
            for (int x = 0; x < ship.getLength(); x++) {
                markers[fieldX + x][fieldY].setAsShip(ships.get(ships.size() - 1));
            }
        } else {
            for (int y = 0; y < ship.getLength(); y++) {
                markers[fieldX][fieldY + y].setAsShip(ships.get(ships.size() - 1));
            }
        }
    }
}
