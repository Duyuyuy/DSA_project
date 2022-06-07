import java.awt.*;
import java.util.ArrayList;

public class Ship {
    public enum ShipPlacementColour {
    	Valid,
    	Invalid,
    	Placed
    }
    
    private Position fieldPosition;
    private Position drawPosition;
    private int length;
    private boolean isHorizontal;
    private int destroyedSections;
    private ShipPlacementColour shipPlacementColour;

    public Ship(Position fieldPosition, Position drawPosition, int length, boolean isHorizontal) {
        this.fieldPosition = fieldPosition;
        this.drawPosition = drawPosition;
        this.length = length;
        this.isHorizontal = isHorizontal;
        destroyedSections = 0;
        shipPlacementColour = ShipPlacementColour.Placed;
    }

    public void paint(Graphics g) {
        if (shipPlacementColour == ShipPlacementColour.Placed) {
            g.setColor(Color.DARK_GRAY);
        } else {
            g.setColor(shipPlacementColour == ShipPlacementColour.Valid ? Color.GREEN : Color.RED);
        }
        if (isHorizontal) {
        	paintHorizontal(g);
        }
        else {
        	paintVertical(g);
        }
    }

    public void setShipPlacementColour(ShipPlacementColour shipPlacementColour) {
        this.shipPlacementColour = shipPlacementColour;
    }

    public void toggleIsVertical() {
        isHorizontal = !isHorizontal;
    }

    public void destroySection() {
        destroyedSections++;
    }

    public boolean isDestroyed() {
    	return destroyedSections >= length;
    }

    public void setDrawPosition(Position gridPosition, Position drawPosition) {
        this.drawPosition = drawPosition;
        this.fieldPosition = gridPosition;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    public int getLength() {
        return length;
    }

    public ArrayList<Position> getCoor() {
        ArrayList<Position> result = new ArrayList<>();
        if (isHorizontal) {
            for (int x = 0; x < length; x++) {
                result.add(new Position(fieldPosition.x + x, fieldPosition.y));
            }
        } else {
            for (int y = 0; y < length; y++) {
                result.add(new Position(fieldPosition.x, fieldPosition.y + y));
            }
        }
        return result;
    }

    public void paintVertical(Graphics g) {
        int boatWidth = (int)(Grid.SIZE * 0.8);
        int boatLeftX = drawPosition.x + Grid.SIZE / 2 - boatWidth / 2;
        
        g.fillPolygon(new int[]{drawPosition.x + Grid.SIZE / 2, boatLeftX, boatLeftX + boatWidth},
                	  new int[]{drawPosition.y + Grid.SIZE / 4, drawPosition.y + Grid.SIZE, drawPosition.y + Grid.SIZE}, 3);
        g.fillRect(boatLeftX, drawPosition.y + Grid.SIZE, boatWidth, (int)(Grid.SIZE * (length - 1.2)));
    }

    public void paintHorizontal(Graphics g) {
        int boatWidth = (int)(Grid.SIZE * 0.8);
        int boatTopY = drawPosition.y + Grid.SIZE / 2 - boatWidth / 2;
        
        g.fillPolygon(new int[]{drawPosition.x + Grid.SIZE / 4, drawPosition.x + Grid.SIZE, drawPosition.x + Grid.SIZE},
                      new int[]{drawPosition.y + Grid.SIZE / 2, boatTopY, boatTopY + boatWidth}, 3);
        g.fillRect(drawPosition.x + Grid.SIZE, boatTopY, (int)(Grid.SIZE * (length - 1.2)), boatWidth);
    }
}
