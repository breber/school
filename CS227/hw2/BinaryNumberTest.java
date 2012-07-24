package hw2;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


/**
 * A JUnit test for the Binary Number Class
 * 
 * @author brianreber
 */
public class BinaryNumberTest {

	private int[] n =  {27,18,29,59,110, 230, 42, 135, 9, 146, 105, 121, 121,869,545,204};
	private int[] n1 = {5 ,10,13,3 ,23 , 6  , 6 , 5,   3, 101, 57, 27, 10,2,545,873};

	private BinaryNumber num;
	private BinaryNumber num1;
	private BinaryNumber expected;

	@Test
	public final void testAdd() {
		System.out.println("ADD");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			num1 = new BinaryNumber(n1[i]);
			expected = new BinaryNumber(n[i] + n1[i]);
			
			BinaryNumber add = num.add(num1);
			
			try {
				
				assertTrue(add + " Expected: " + expected, add.compareTo(expected) == 0);
//				System.out.println(num.add(num1) + " Expected: " + expected);
				
			} catch (AssertionError e) {
				System.err.println(e.getMessage());
				numErrs++;
			}
		}
		
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		
		System.out.println();
	}

	@Test
	public final void testTimes() {
		System.out.println("TIMES");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			num1 = new BinaryNumber(n1[i]);
			expected = new BinaryNumber(n[i] * n1[i]);
			
			BinaryNumber times = num.times(num1);
			
			try {
				assertTrue(times + " Expected: " + expected, times.compareTo(expected) == 0);
//				System.out.println(num.times(num1) + " Expected: " + expected);
			} catch (AssertionError e) {
				System.err.println(e.getMessage());
				numErrs++;
			}
		}
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		System.out.println();
	}

	@Test
	public final void testDifference() {
		System.out.println("DIFFERENCE");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			num1 = new BinaryNumber(n1[i]);
			expected = new BinaryNumber(n[i] - n1[i]);
			
			BinaryNumber dif = num.difference(num1);
			try {
				assertTrue(dif + " Expected: " + expected, dif.compareTo(expected) == 0);
//				System.out.println(num.difference(num1) + " Expected: " + expected);
			} catch (AssertionError e) {
				System.err.println(e.getMessage());
				numErrs++;
			}
		}
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		System.out.println();
	}

	@Test
	public final void testDivide() {
		System.out.println("DIVIDE");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			num1 = new BinaryNumber(n1[i]);
			expected = new BinaryNumber(n[i] / n1[i]);
			
			BinaryNumber div = num.divide(num1);
			
			try {
				assertTrue(div + " Expected: " + expected, div.compareTo(expected) == 0);
//				System.out.println(num.divide(num1) + " Expected: " + expected);
			} catch (AssertionError e) {
				System.err.println(i + " " +e.getMessage());
				numErrs++;
			}
		}
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		System.out.println();
	}

	@Test
	public final void testModulus() {
		System.out.println("MOD");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			num1 = new BinaryNumber(n1[i]);
			expected = new BinaryNumber(n[i] % n1[i]);
			
			BinaryNumber mod = num.modulus(num1);
			
			try {
				assertTrue(mod + " Expected: " + expected, mod.compareTo(expected) == 0);
//				System.out.println(num.divide(num1) + " Expected: " + expected);
			} catch (Exception e) {
				System.err.println(i + " " +e.getMessage());
				numErrs++;
			}
		}
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		System.out.println();
	}

	@Test
	public final void testToDecimal() {
		System.out.println("TODECIMAL");
		int numErrs = 0;
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n[i]);
			String numDec = num.toDecimal();
			try {
				assertTrue(numDec + " Expected: " + n[i], numDec.equals(String.valueOf(n[i])));
//				System.out.println(num.toDecimal() + " Expected: " + String.valueOf(n[i]));
			} catch (AssertionError e) {
				System.err.println(e.getMessage());
				numErrs++;
			}
		}
		
		for (int i = 0; i < n.length; i++)
		{
			num = new BinaryNumber(n1[i]);
			String numDec = num.toDecimal();
			
			try {
				assertTrue(numDec + " Expected: " + n1[i], numDec.equals(String.valueOf(n1[i])));
//				System.out.println(num.toDecimal() + " Expected: " + String.valueOf(n[i]));
			} catch (AssertionError e) {
				System.err.println(e.getMessage());
				numErrs++;
			}
		}
		if (numErrs != 0) throw new AssertionError(numErrs + " errors");
		System.out.println();
	}
}
