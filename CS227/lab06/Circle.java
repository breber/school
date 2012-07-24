package lab06;

/**
 * A circle with radius and origin.
 */
public class Circle {

	/**
	 * The circle's radius.
	 */
	private double radius;

	/**
	 * The circle's origin.
	 */
	private double x;
	private double y;

	/**
	 * Create a circle with the specified radius and origin.
	 * 
	 * @param givenRadius
	 * The circle's radius.
	 * 
	 * @param givenX
	 * The x-coordinate of the circle's origin.
	 * 
	 * @param givenY
	 * The y-coordinate of the circle's origin.
	 */
	public Circle(double givenRadius,
			double givenX,
			double givenY) {     
		radius = givenRadius;
		x = givenX;
		y = givenY;
	}

	/**
	 * Determine if this circle is equivalent to another, having the
	 * same origin and radius.
	 * 
	 * @param other
	 * Circle to compare to.
	 * 
	 * @return
	 * True if the circles are equivalent.
	 */
	public boolean equals(Circle other) {
		double radiusDiff = other.radius - radius;
		double xDiff = other.x - x;
		double yDiff = other.y - y;//This was an error - it was x instead of y

		// Comparing doubles with == is not always possible because of the
		// limited precision that these can numbers can be stored with.  So,
		// we instead check to see if the three doubles are within a very
		// tiny threshold of each other.
		return Math.abs(radiusDiff) < 1e-6f &&
		Math.abs(xDiff) < 1e-6f &&
		Math.abs(yDiff) < 1e-6f;
	}

	/**
	 * Determine if this circle intersects another. Two circles intersect if
	 * the sum of their radii is greater than the distance between their
	 * origins.
	 *  
	 * @param other
	 * Circle to check for intersection with.
	 * 
	 * @return
	 * True if the circles intersect.
	 */
	public boolean intersects(Circle other) {
		double yDiff = other.y - y;
		double xDiff = other.x - x;
		double originDistance = Math.sqrt(yDiff * yDiff + xDiff * xDiff);
		double sumRadii = radius + other.radius;

		return originDistance < sumRadii; //Error
	}

	/**
	 * Get a textual representation of this circle reporting its
	 * origin and radius.
	 * 
	 * @return
	 * A textual representation of this circle.
	 */
	@Override
	public String toString() {
		String text = "Circle[radius: " + radius + ",;";
		text += " origin=(";
		text += x + "," + y;  // We were just resetting text instead of concatenating
		text += ")]";
		return text;
	}
}
