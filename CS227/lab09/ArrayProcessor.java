package lab09;

/**
 * @author brianreber
 *
 */
public class ArrayProcessor {
	/**
	    Replace each element in a series with its processed version.

	    @param series
	    Collect of elements to process.

	    @param op
	    Operation to perform on each element.
	 */
	public static void processElements(double[] series,
			ScalarOperation op) {
		for (int i = 0; i < series.length; i++) {
			series[i] = op.process(series[i]);
		}
	}

	public static void main(String[] args) {
		double[] temperatures = {0.0, 100.0, -40.0, 50.0};
		processElements(temperatures, new CelsiusToFahrenheit());
		System.out.println(java.util.Arrays.toString(temperatures));
	}
}
