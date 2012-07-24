package pre2;

import java.awt.Color;
import java.util.Scanner;


/**
 * Utilities for Pre2
 * 
 * @author brianreber
 *
 */
public class Utilities {

	/**
	 * Parses a string parameter and returns the color from the parsed value
	 * @param rgb
	 * the String containing either hex or R G B value
	 * @return
	 * The Color representing the value in the rgb string
	 */
	public static Color getColor(String rgb)
	{
		Color col;
		
		if (rgb.contains(" "))
		{
			Scanner scan = new Scanner(rgb);
			
			int red = scan.nextInt();
			int green = scan.nextInt();
			int blue = scan.nextInt();
			col = new Color(red,green,blue);
		}	else {
			col = new Color(Integer.parseInt(rgb, 16));
		}
		
		return col;
	}
	
	/**
	 * Calculates a factorial of the given number
	 * 
	 * @param num
	 * The number that we want to find the factorial of
	 * @return
	 * The factorial of the parameter
	 */
	public static int calculateFactorial(int num)
	{
		int fact = 1;
		
		if (num != 0)
		{
			for (int i = 1; i <= num; i++)
			{
				fact *= i;
			}
		}
		else fact = 1;
		
		return fact;
	}
	
}
