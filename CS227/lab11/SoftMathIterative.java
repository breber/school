package lab11;

/**
 * @author brianreber
 *
 */
public class SoftMathIterative implements SoftMath {

	/* (non-Javadoc)
	 * @see lab11.SoftMath#multiply(int, int)
	 */
	@Override
	public int multiply(int a, int b) {
		
		if (b == 0 || a == 0)
			return 0;
		
		int temp = 0;
		
		for (int i = 0; i < b; i++)
		{
			temp += a;
		}
		
		return temp;
	}

}
