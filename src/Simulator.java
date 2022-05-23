import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Simulator {
    private Field mF, oF;
    private int turn = 4;
    private int size = 5;
    private int ships = 2;
    private ArrayList<Point> points;
    private ArrayList<ArrayList<Point>> prio;
    private Point cur;

    public Simulator(ArrayList<Ship> ship) {
        mF = new Field(size);
        oF = new Field(size);
        points = new ArrayList<>();
        prio = new ArrayList<>();

        for (Ship s : ship) {
            mF.setShip(s);
        }
        addShip(oF);

        for (int i = 0; i < size; i += 3) {
            for (int j = 0; j < size; j += 3) {
                if (i < size && j < size) {
                    points.add(new Point(i, j));
                }
                if (i + 1 < size && j + 1 < size) {
                    points.add(new Point(i + 1, j + 1));
                }
                if (i + 2 < size && j + 2 < size) {
                    points.add(new Point(i + 2, j + 2));
                }
            }
        }
    }

    public void test() {
        Random rd = new Random();

        while (points.size() != 0) {
            int choice = Math.abs(rd.nextInt()) %  points.size();

            if(mF.getField()[points.get(choice).getX()][points.get(choice).getY()] == '-') {
                mF.getField()[points.get(choice).getX()][points.get(choice).getY()] = 'x';
            } else {
                mF.getField()[points.get(choice).getX()][points.get(choice).getY()] = 'c';
            }

            points.remove(choice);
            mF.show();

            System.out.println();
        }
    }

    public void t() {
        while (mF.getRemain() != 0 && points.size() != 0) {
            cTurn();
        }
    }

    public void play() {
        display();
        System.out.println();

        while (mF.getRemain() != 0 && oF.getRemain() != 0) {
            pTurn();
            if (oF.getRemain() == 0) {
                break;
            }

            cTurn();
            if (mF.getRemain() == 0) {
                break;
            }
        }
    }

    private void addShip(Field f) {
        while (f.getRemain() < ships) {
            addShipRandomly(f);
        }
    }

    private void addShipRandomly(Field f) {
        double dir = Math.random();
        int sX, sY, eX, eY;
        
        if (dir >= 0.5) {
            sX = (int)(Math.random() * (size));
            sY = (int)(Math.random() * (size - 4));
            eX = sX;

            double siz = Math.random();
            if (siz >= 0.5) {
                eY = sY + 2;
            } else {
                eY = sY + 3;
            }
        } else {
            sX = (int)(Math.random() * (size - 4));
            sY = (int)(Math.random() * (size));
            eY = sY;

            double siz = Math.random();
            if (siz >= 0.5) {
                eX = sX + 2;
            } else {
                eX = sX + 3;
            }
        }

        f.setShip(new Ship(sX, sY, eX, eY));
    }

    private void pTurn() {
        Scanner sc = new Scanner(System.in);
        int x, y;

        for (int i = 0; i < turn; i++) {
            System.out.print("Input x and y: ");
            x = sc.nextInt();
            y = sc.nextInt();

            oF.hit(x, y);

            display();
            if (oF.getRemain() == 0) {
                System.out.println("Player win!");
                break;
            }
            System.out.println();
        }
    }

    private void cTurn() {
        for (int i = 0; i < turn; i++) {
            if (prio.size() == 0) {
                playNormal();

                display();
                if (mF.getRemain() == 0) {
                    System.out.println("Computer win!");
                    break;
                }
                System.out.println();
            } else {
                playTarget();

                display();
                if (mF.getRemain() == 0) {
                    System.out.println("Computer win!");
                    break;
                }
                System.out.println();
            }
            
            try {
            	Thread.sleep(1000);
            } catch (Exception e) {}
        }
    }

    public void playNormal() {
        if (points.size() != 0) {
            Random rd = new Random();
            int choice = Math.abs(rd.nextInt()) % points.size();
            int x = points.get(choice).getX();
            int y = points.get(choice).getY();

            System.out.println("Attacked at [" + x + ", " + y + "]");

            remove(x, y);

            if (mF.hit(x, y)) {
                cur = new Point(x,y);

                ArrayList<Point> t1 = new ArrayList<>();
                ArrayList<Point> t2 = new ArrayList<>();
                ArrayList<Point> t3 = new ArrayList<>();
                ArrayList<Point> t4 = new ArrayList<>();

                for (int i = 1; i < 4; i++) {
                    if (x - i >= 0) {
                        t1.add(new Point(x - i, y));
                    }

                    if (x + i < size) {
                        t3.add(new Point(x + i, y));
                    }

                    if (y + i < size) {
                        t2.add(new Point(x, y + i));
                    }

                    if (y - i >= 0) {
                        t4.add(new Point(x, y - i));
                    }
                }
                if (!t1.isEmpty()) {
                    prio.add(t1);
                }
                if (!t2.isEmpty()) {
                    prio.add(t2);
                }
                if (!t3.isEmpty()) {
                    prio.add(t3);
                }
                if (!t4.isEmpty()) {
                    prio.add(t4);
                }
            }
        }
    }

    public void playTarget() {
        Random rd = new Random();
        boolean toChange = false;
        int choice = 0;

        if (!toChange) {
            int rand = Math.abs(rd.nextInt()) % prio.size();
            while (prio.get(rand).size() == 0) {
                rand = Math.abs(rd.nextInt()) % prio.size();
            }

            choice = rand;
        }

        int x = prio.get(choice).get(0).getX();
        int y = prio.get(choice).get(0).getY();
        remove(x, y);

        System.out.println("Attacked at [" + x + ", " + y + "]");

        if (mF.hit(x, y)) {
            prio.get(choice).remove(0);

            if (prio.get(choice).size() == 0) {
                prio.remove(choice);
            }

            if (cur.getX() == x) {
                for (int i = 0; i < prio.size(); i++) {
                    if (prio.get(i).get(0).getX() != x) {
                        prio.remove(i);
                    }
                }
            }

            if (cur.getY() == y) {
                for (int i = 0; i < prio.size(); i++) {
                    if (prio.get(i).get(0).getY() != y) {
                        prio.remove(i);
                    }
                }
            }

            toChange = false;
        } else {
            prio.remove(choice);
            
            toChange = true;
        }
    }

    private void remove(int x, int y) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).getX() == x && points.get(i).getY() == y) {
                points.remove(i);
                return;
            }
        }
    }

    public void display() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(mF.getField()[i][j] + " ");
            }

            System.out.print("      ");

            for (int j = 0; j < size; j++) {
                if (oF.getField()[i][j] == 'o') {
                    System.out.print("- ");
                } else {
                    System.out.print(oF.getField()[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}
