public class Point {
    private int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public boolean isEqual(Point p) {
        return (x == p.getX() && y == p.getY()) ? true : false;
    }

    public boolean isEqual(int x, int y) {
        return (this.x == x && this.y == y) ? true : false;
    }
}