package lab09;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * @author brianreber
 *
 */
public class RectangleTest {

	/**
	 * Test method for {@link lab09.Rectangle#Rectangle(int, int)}.
	 */
	@Test
	public final void testRectangle() {
		Rectangle r = new Rectangle(5, 4);
		assertEquals("width", 5, r.getWidth());
		assertEquals("height", 4, r.getHeight());
	}

	/**
	 * Test method for {@link lab09.Rectangle#getArea()}.
	 */
	@Test
	public final void testGetArea() {
		Rectangle r = new Rectangle(5, 4);
		assertEquals("area", 20, r.getArea());
	}

	/**
	 * Test method for {@link lab09.Rectangle#getPerimeter()}.
	 */
	@Test
	public final void testGetPerimeter() {
		Rectangle r = new Rectangle(5, 4);
		assertEquals("perimeter", 18, r.getPerimeter());
	}

	/**
	 * Test method for {@link lab09.Rectangle#isSquare()}.
	 */
	@Test
	public final void testIsSquare() {
		Rectangle r = new Rectangle(5, 5);
		assertTrue("square", r.isSquare());
		r = new Rectangle(3, 5);
		assertFalse("non-square", r.isSquare());
	}

}
