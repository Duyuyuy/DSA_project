import java.util.ArrayList;

public class Ship {
    private ArrayList<Point> pList;
    private int length;

    public Ship(int x1, int y1, int x2, int y2) {
        pList = new ArrayList<>();
        length = 0;

        if (x1 == x2) {
			if (y1 < y2) {
				while (y1 <= y2) {
					pList.add(new Point(x1, y1));
					y1++;
					length++;
				}		
			} else {
				while (y1 >= y2) {
					pList.add(new Point(x2, y2));
					y2++;
					length++;
				}
			}
		} else if (y1 == y2) {
			if (x1 < x2) {
				while (x1 <= x2) {
					pList.add(new Point(x1, y1));
					x1++;
					length++;
				}
			} else {
				while (x1 >= x2) {
					pList.add(new Point(x2, y2));
					x2++;
					length++;
				}
			}
		}
    }

    public ArrayList<Point> getPList() {
        return pList;
    }

    public int getLength() {
        return length;
    }

    public boolean isValid() {
        return length <= 0 ? false : true;
    }

    public boolean isHit(int x, int y) {
        return getI(x, y) == -1 ? false : true;
    }

    public void remove(int x, int y) {
        if (getI(x, y) != -1) {
            pList.remove(getI(x, y));
        }
    }

    public void display() {
        if (!pList.isEmpty()) {
            for (Point p : pList) {
                System.out.println("[" + p.getX() + ", " + p.getY() + "]");
            }
        } else {
            System.out.println("Empty");
        }

        System.out.println();
    }

    private int getI(int x, int y) {
        for (int i = 0; i < pList.size(); i++) {
            if (pList.get(i).getX() == x && pList.get(i).getY() == y) {
                return i;
            }
        }

        return -1;
    }
}
