import java.util.ArrayList;

public class Field {
    private char[][] field;
    private ArrayList<Ship> sList;
    private int size, remain;

    public Field(int size) {
        this.size = size;
        field = new char[size][size];
        sList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                field[i][j] = '-';
            }
        }
    }

    public char[][] getField() {
        return field;
    }

    public int getRemain() {
        return remain;
    }

    public void setRemain(int remain) {
        this.remain = remain;
    }

    public int getSize() {
        return size;
    }

    public void setShip(Ship s) {
        if (isClear(s)) {
            for (Point p : s.getPList()) {
                field[p.getX()][p.getY()] = 'o';
            }

            sList.add(s);
            remain++;
        }
    }

    public void setAt(int x, int y, char ch) {
        field[x][y] = ch;
    }

    public boolean hit(int x, int y) {
        for (Ship s : sList) {
            if (s.isHit(x, y)) {
                field[x][y] = 'c';
                s.remove(x, y);

                if (s.getPList().size() == 0) {
                    sList.remove(s);
                    remain--;
                }

                return true;
            }
        }

        if (field[x][y] != 'c') {
            field[x][y] = 'x';
        }
        return false;
    }

    public void show() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(field[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void showOp() {
        for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				if (this.field[i][j] != 'o') {
					System.out.print(this.field[i][j] + " ");
				} else {
					System.out.print("-" + " ");
				}
			}
			System.out.println();
		}
    }

    private boolean isClear(Ship s) {
        if (s.isValid()) {
            for (Point p : s.getPList()) {
                if (field[p.getX()][p.getY()] == 'o') {
                    return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }
}
