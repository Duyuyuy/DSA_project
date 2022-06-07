import java.util.ArrayList;
import java.util.Collections;

public class AI{
    private ArrayList<Position> hitAt;
    private ArrayList<Position> validPositions;
    public Grid playerGrid;

    public AI(Grid playerGrid) {
    	this.playerGrid = playerGrid;
    	hitAt = new ArrayList<>();
    	
        createValidPositionList();
        Collections.shuffle(validPositions);
    }

    public void reset() {
    	createValidPositionList();
        hitAt.clear();
        Collections.shuffle(validPositions);
    }

    public Position selectMove() {
        Position position;
        
        if (hitAt.size() > 0) {
        	position = getAttack();
        } else {
        	position = findPosition();
        }
        updateHits(position);
        validPositions.remove(position);
        return position;
    }

    private Position getAttack() {
        ArrayList<Position> suggested = getAdjPosition();
        
        for(Position possible : suggested) {
            if (isInDirection(possible, Position.LEFT)) {
            	return possible;
            }
            if (isInDirection(possible, Position.RIGHT)) {
            	return possible;
            }
            if (isInDirection(possible, Position.DOWN)) {
            	return possible;
            }
            if (isInDirection(possible, Position.UP)) {
            	return possible;
            }
        }
        Collections.shuffle(suggested);
        return suggested.get(0);
    }

    private Position findPosition() {
    	Position position = validPositions.get(0);
        int max = -1;
        int count;
   
        for (int i = 0; i < validPositions.size(); i++) {
            count = getAdjFree(validPositions.get(i));
            if (count == 4) {
                return validPositions.get(i);
            } else if (count > max) {
                max = count;
                position = validPositions.get(i);
            }
        }
        return position;
    }

    private int getAdjFree(Position position) {
        ArrayList<Position> adjCells = getAdjCells(position);
        int count = 0;
        
        for (Position adjCell : adjCells) {
            if (!playerGrid.getMarkerAt(adjCell).isMarked()) {
                count++;
            }
        }
        return count;
    }

    private boolean isInDirection(Position start, Position direction) {
        Position p = new Position(start);
        
        p.add(direction);
        if (!hitAt.contains(p)) {
        	return false;
        }
        p.add(direction);
        if (!hitAt.contains(p)) {
        	return false;
        }
        return true;
    }

    private ArrayList<Position> getAdjPosition() {
        ArrayList<Position> result = new ArrayList<>();
        
        for (Position p : hitAt) {
            ArrayList<Position> adjPositions = getAdjCells(p);
            
            for (Position adjP : adjPositions) {
                if (!result.contains(adjP) && validPositions.contains(adjP)) {
                    result.add(adjP);
                }
            }
        }

        return result;
    }

    private ArrayList<Position> getAdjCells(Position position) {
        ArrayList<Position> result = new ArrayList<>();
        
        if (position.x != 0) {
            Position left = new Position(position);
            left.add(Position.LEFT);
            result.add(left);
        }
        if (position.x != Grid.FIELD_WIDTH - 1) {
            Position right = new Position(position);
            right.add(Position.RIGHT);
            result.add(right);
        }
        if (position.y != 0) {
            Position up = new Position(position);
            up.add(Position.UP);
            result.add(up);
        }
        if (position.y != Grid.FIELD_HEIGHT - 1) {
            Position down = new Position(position);
            down.add(Position.DOWN);
            result.add(down);
        }
        return result;
    }

    private void updateHits(Position position) {
        Marker marker = playerGrid.getMarkerAt(position);
        
        if(marker.isShip()) {
            hitAt.add(position);
            
            ArrayList<Position> lastShipPos = marker.getShip().getCoor();
            boolean hitAll = hasPos(lastShipPos, hitAt);
            
            if (hitAll) {
                for (Position shipPosition : lastShipPos) {
                    for (int i = 0; i < hitAt.size(); i++) {
                        if (hitAt.get(i).equals(shipPosition)) {
                            hitAt.remove(i);
                            break;
                        }
                    }
                }
            }
        }
    }

    private boolean hasPos(ArrayList<Position> search, ArrayList<Position> in) {
        for (Position p : search) {
            boolean found = false;
            
            for (Position inP : in) {
                if (inP.equals(p)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
            	return false;
            }
        }
        return true;
    }
    
    private void createValidPositionList() {
        validPositions = new ArrayList<>();
        for (int x = 0; x < Grid.FIELD_WIDTH; x++) {
            for (int y = 0; y < Grid.FIELD_HEIGHT; y++) {
                validPositions.add(new Position(x,y));
            }
        }
    }
}
