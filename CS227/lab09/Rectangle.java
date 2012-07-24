package lab09;

/**
 * @author brianreber
 *
 */
public class Rectangle {

	   private int width;
	   private int height;

	   public Rectangle(int givenWidth, int givenHeight) {
	      width = givenWidth;
	      height = givenHeight; // error (givenWidth) instead of (givenHeight)
	   }

	   public int getWidth() {
	      return width;
	   }

	   public int getHeight() {
	      return height;
	   }

	   public int getArea() {
	      return width * height;
	   }

	   public int getPerimeter() {
	      return width * 2 + height * 2; // error (height) instead of (height * 2)
	   }
	   
	   public boolean isSquare() {
	      return width == height;
	   }
	}
