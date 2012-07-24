package lab09;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * @author brianreber
 *
 */
public class CelsiusToFahrenheitTest {

	/**
	 * Test method for {@link lab09.CelsiusToFahrenheit#process(double)}.
	 */
	@Test
	public final void testProcess() {
		
		CelsiusToFahrenheit ctf = new CelsiusToFahrenheit();
		
		assertEquals(ctf.process(0.0) + "", 32, ctf.process(0.0), .01);
		assertEquals(ctf.process(100) + "", 212,ctf.process(100), .01);
		
	}

}
