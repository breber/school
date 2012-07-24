package lab11;

/**
 * @author brianreber
 *
 */
public class Checkpoint1 {

	/**
	 * Recursively compute base to the exponent power.
	 * 
	 * @param base
	 * @param exponent
	 * @return
	 * Base ^ exponent
	 */
	public static int power(int base, int exponent)
	{
		if (exponent == 0)
			return 1;
		return base * power(base, exponent - 1);
	}
	
	/**
	 * Get the number of balls in a pyramid with the given amount of levels
	 * 
	 * @param levels
	 * @return
	 * The number of balls in the pyramid
	 */
	public static int getPyramidCount(int levels)
	{
		if (levels == 1)
			return 1;
		
		return power(levels, 2) + getPyramidCount(levels - 1);
	}
	
	
	public static void main(String[] args) {
		System.out.println("2 ^ 8 = " + power(2,8));
		System.out.println("A pyramid with 7 levels has " + getPyramidCount(7)
							+ " balls.");
	}
	
}
