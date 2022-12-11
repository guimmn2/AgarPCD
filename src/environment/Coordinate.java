package environment;

import java.io.Serializable;
import java.util.Random;

public class Coordinate implements Serializable {
	public final int x;
	public final int y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}

	@Override
	public boolean equals(Object obj) {
		Coordinate other = (Coordinate) obj;
		return other.x==x && other.y == y;
	}
	
	public double distanceTo(Coordinate other) {
		double dx = y - other.y;
		double dy = x - other.x;
		return Math.sqrt(dx * dx + dy * dy);
	}

	public Coordinate translate(Coordinate vector) {
		return new Coordinate(x+vector.x, y+vector.y);
	}
	
	public static Coordinate randomDirection() {
		Random r = new Random();
		switch (r.nextInt(4 - 1 + 1) + 1) {
		case 1:
			return Direction.UP.getVector();
		case 2:
			return Direction.DOWN.getVector();
		case 3:
			return Direction.LEFT.getVector();
		default:
			return Direction.RIGHT.getVector();
		}
	}
}
