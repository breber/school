package lab09;

/**
 * @author brianreber
 *
 */
public class CelsiusToFahrenheit implements ScalarOperation {

	/* (non-Javadoc)
	 * @see lab09.ScalarOperation#process(double)
	 */
	@Override
	public double process(double original) {
		
		return original * 9 / 5.0 + 32;
	}

}
