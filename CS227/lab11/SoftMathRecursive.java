package lab11;

/**
 * @author brianreber
 *
 */
public class SoftMathRecursive implements SoftMath {

	/* (non-Javadoc)
	 * @see lab11.SoftMath#multiply(int, int)
	 */
	@Override
	public int multiply(int a, int b) {
		if (b == 0 || a == 0)
			return 0;
		
		return a + multiply(a, b-1);
	}

}
