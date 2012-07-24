package hw3;

/**
 * This Effect will flip the image about the y axis -
 * Therefore reversing the columns.
 * 
 * @author Brian Reber
 */
public class MyEffect implements Effect{

	/**
	 * Applies this effect to the given Image.
	 * 
	 * @param image
	 */
	public void apply(Image image) {
		Image tempImage = new Image(image.getWidth(), image.getHeight());

		//Copies the value of image into the temporary image
		for (int col = 0; col < image.getWidth(); col++)
		{
			for (int row = 0; row < image.getHeight(); row++)
			{
				tempImage.setPixel(col, row, image.getPixel(col, row));
			}
		}

		//Reverses the columns by using the tempImage
		for (int col = 0; col < image.getWidth(); col++)
		{
			for (int row = 0; row < image.getHeight(); row++)
			{
				image.setPixel(col, row, tempImage.getPixel(image.getWidth() - 1 - col, row));
			}
		}
	}
}
