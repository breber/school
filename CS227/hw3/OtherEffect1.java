package hw3;

import java.awt.Color;

/**
 * This effect inverts an image, so that white is black, red is blue, etc
 */
public class OtherEffect1 implements Effect
{
	public void apply(Image image)
	{
		int height = image.getHeight(), width = image.getWidth(),
		red, green, blue;
		Color pixel;

		for (int row = 0; row < height; row++)
		{
			for (int col = 0; col < width; col++)
			{
				pixel = image.getPixel(col, row);
				red = 255 - pixel.getRed();
				green = 255 - pixel.getGreen();
				blue = 255 - pixel.getBlue();
				image.setPixel(col, row, new Color(red, green, blue));
			}
		}
	}
}
