import java.awt.*;

public class StatusPanel extends Cell{
    private final Font font = new Font("Arial", Font.BOLD, 20);
    private String topLine;
    private String bottomLine;
    
    public StatusPanel(Position position, int width, int height) {
        super(position, width, height);
        reset();
    }

    public void reset() {
        topLine = "Place your Ships";
        bottomLine = "Press Z to rotate.";
    }

    public void showGameOver(boolean playerWon) {
        topLine = (playerWon) ? "Game Over! You Win" : "Game Over! You Lost";
        bottomLine = "Press R to restart";
    }

    public void setTop(String message) {
        topLine = message;
    }

    public void setBottom(String message) {
        bottomLine = message;
    }

    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(position.x, position.y, width, height);
        g.setColor(Color.BLACK);
        g.setFont(font);
        int strWidth = g.getFontMetrics().stringWidth(topLine);
        g.drawString(topLine, position.x + width / 2 - strWidth / 2, position.y + 20);
        strWidth = g.getFontMetrics().stringWidth(bottomLine);
        g.drawString(bottomLine, position.x + width / 2 - strWidth / 2, position.y + 40);
    }
}
