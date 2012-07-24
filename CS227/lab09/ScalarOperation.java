package lab09;

/**
 * @author brianreber
 *
 */
public interface ScalarOperation {
	/**
	    Given a value, process it to produce a new value.

	    @param original
	    The scalar value to process.

	    @return
	    The new value which results from processing the original value.
	 */
	public double process(double original);
}
