import java.util.ArrayList;

public class Main {
	public static void main(String[] args) {
		ArrayList<Ship> s = new ArrayList<>();
        s.add(new Ship(1, 0, 1, 2));
        s.add(new Ship(1, 4, 4, 4));


        Simulator si = new Simulator(s);
        si.play();
	}
}
