package lab06;

public class CircleTest {

	public static void main(String[] args) {
		Circle a = new Circle(10.0, 0.3, 0.4);
		Circle b = new Circle(10.0, 0.3, 0.4);
		examineCircles(a, b);

		a = new Circle(1.0, 10.0, 20.0);
		b = new Circle(2.0, 10.0, 10.0);
		examineCircles(a, b);
	}

	private static void examineCircles(Circle c1,
			Circle c2) {
		System.out.println("Circle c1: " + c1.toString());
		System.out.println("Circle c2: " + c2);

		// See if c1 and c2 are congruent, i.e., they represent the same circle.
		if (c1.equals(c2)) { //This was an error!
			System.out.println("a and b are equivalent.");
		} else {
			System.out.println("a and b are not equivalent.");
		}

		if (c1.intersects(c2)) {
			System.out.println("a intersects b.");
		} else {
			System.out.println("a does not intersect b.");
		}

		System.out.println();
	}
}
