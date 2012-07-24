/**
 * 
 */
package hw3;

import java.awt.Color;
import java.util.Random;

/**
 * @author brianreber
 *
 */
public class RandomPPMGen extends Image{

	private Random rand = new Random();
	
	public RandomPPMGen(int width, int height)
	{
		super(width, height);
	}
	
	public void fill()
	{
		for (int i = 0; i < getWidth(); i++)
		{
			for (int j = 0; j < getHeight(); j++)
			{
				int red = rand.nextInt(255);
				int green = rand.nextInt(255);
				int blue = rand.nextInt(255);
				setPixel(i, j, new Color(red,green,blue));
			}
		}
	}
}
