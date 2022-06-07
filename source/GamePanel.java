import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GamePanel extends JPanel implements MouseListener, MouseMotionListener {
    public enum GameState {
    	Placing,
    	Playing,
    	GameOver
    }

    private StatusPanel statusPanel;
    private Grid computer;
    private Grid player;
    private AI aiController;
    private Ship ship;
    private Position tmpPosition;
    private int shipNo;
    private GameState gameState;

    public GamePanel() {
        computer = new Grid(0,0);
        player = new Grid(computer.getWidth() + 50, 0);
        setBackground(Color.LIGHT_GRAY);
        setPreferredSize(new Dimension(computer.getWidth() + 50 + player.getWidth(), player.getPosition().y + player.getHeight() + 50));
        addMouseListener(this);
        addMouseMotionListener(this);
        aiController = new AI(player);
        statusPanel = new StatusPanel(new Position((player.getWidth() + 50) / 2, computer.getHeight() + 1), computer.getWidth(), 48);
        restart();
    }

    public void paint(Graphics g) {
        super.paint(g);
        computer.paint(g);
        player.paint(g);
        if (gameState == GameState.Placing) {
            ship.paint(g);
        }
        statusPanel.paint(g);
    }

    public void handleInput(int keyCode) {
        if (keyCode == KeyEvent.VK_ESCAPE) {
            System.exit(1);
        } else if (keyCode == KeyEvent.VK_R) {
            restart();
        } else if (gameState == GameState.Placing && keyCode == KeyEvent.VK_Z) {
            ship.toggleIsVertical();
            updateShipPosition(tmpPosition);
        }
        repaint();
    }

    public void restart() {
        computer.reset();
        player.reset();
        player.setShowShips(true);
        aiController.reset();
        tmpPosition = new Position(0, 0);
        ship = new Ship(new Position(0, 0), new Position(player.getPosition().x, player.getPosition().y), Grid.BOAT_SIZES[0], true);
        shipNo = 0;
        updateShipPosition(tmpPosition);
        computer.generateShips();
        statusPanel.reset();
        gameState = GameState.Placing;
    }

    private void tryPlaceShip(Position mousePosition) {
        Position target = player.getPositionInField(mousePosition.x, mousePosition.y);
        
        updateShipPosition(target);
        if (player.canPlaceShipAt(target.x, target.y, Grid.BOAT_SIZES[shipNo], ship.isHorizontal())) {
            placeShip(target);
        }
    }

    private void placeShip(Position target) {
        ship.setShipPlacementColour(Ship.ShipPlacementColour.Placed);
        player.placeShip(ship, tmpPosition.x, tmpPosition.y);
        shipNo++;
        if (shipNo < Grid.BOAT_SIZES.length) {
            ship = new Ship(new Position(target.x, target.y), new Position(player.getPosition().x + target.x * Grid.SIZE, player.getPosition().y + target.y * Grid.SIZE), Grid.BOAT_SIZES[shipNo], true);
            updateShipPosition(tmpPosition);
        } else {
            gameState = GameState.Playing;
            statusPanel.setTop("Attack the Computer!");
            statusPanel.setBottom("Destroy all Ships to win!");
        }
    }

    private void fire(Position mousePosition) {
        Position target = computer.getPositionInField(mousePosition.x, mousePosition.y);
        if (!computer.isPositionMarked(target)) {
            playerTurn(target);
            if(!computer.areAllShipsDestroyed()) {
                computerTurn();
            }
        }
    }

    private void playerTurn(Position target) {
        boolean hit = computer.markPosition(target);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && computer.getMarkerAt(target).getShip().isDestroyed()) {
            destroyed = "(Ship destroyed)";
        }
        statusPanel.setTop("Player " + hitMiss + " At " + target + destroyed);
        if (computer.areAllShipsDestroyed()) {
            gameState = GameState.GameOver;
            statusPanel.showGameOver(true);
        }
    }

    private void computerTurn() {
        Position target = aiController.selectMove();
        boolean hit = player.markPosition(target);
        String hitMiss = hit ? "Hit" : "Missed";
        String destroyed = "";
        if (hit && player.getMarkerAt(target).getShip().isDestroyed()) {
            destroyed = "(Ship Destroyed)";
        }
        statusPanel.setBottom("Computer " + hitMiss + " At " + target + destroyed);
        if (player.areAllShipsDestroyed()) {
            gameState = GameState.GameOver;
            statusPanel.showGameOver(false);
        }
    }

    private void movePlacingShip(Position mousePosition) {
        if (player.isPositionInside(mousePosition)) {
            Position target = player.getPositionInField(mousePosition.x, mousePosition.y);
            updateShipPosition(target);
        }
    }

    private void updateShipPosition(Position target) {
        if (ship.isHorizontal()) {
            target.x = Math.min(target.x, Grid.FIELD_WIDTH - Grid.BOAT_SIZES[shipNo]);
        } else {
            target.y = Math.min(target.y, Grid.FIELD_HEIGHT - Grid.BOAT_SIZES[shipNo]);
        }
        ship.setDrawPosition(new Position(target), new Position(player.getPosition().x + target.x * Grid.SIZE, player.getPosition().y + target.y * Grid.SIZE));
        tmpPosition = target;
        if (player.canPlaceShipAt(tmpPosition.x, tmpPosition.y, Grid.BOAT_SIZES[shipNo], ship.isHorizontal())) {
            ship.setShipPlacementColour(Ship.ShipPlacementColour.Valid);
        } else {
            ship.setShipPlacementColour(Ship.ShipPlacementColour.Invalid);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Position mousePosition = new Position(e.getX(), e.getY());
        if (gameState == GameState.Placing && player.isPositionInside(mousePosition)) {
            tryPlaceShip(mousePosition);
        } else if (gameState == GameState.Playing && computer.isPositionInside(mousePosition)) {
            fire(mousePosition);
        }
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (gameState != GameState.Placing) {
        	return;
        }
        movePlacingShip(new Position(e.getX(), e.getY()));
        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    @Override
    public void mouseDragged(MouseEvent e) {}
}
