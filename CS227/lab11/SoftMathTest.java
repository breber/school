package lab11;

import static org.junit.Assert.assertEquals;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class SoftMathTest {

   private SoftMath calculator;
   private static final int TRIAL_COUNT = 1000;
   
   @Before
   public void setup() {
//	   calculator = new SoftMathRecursive();
	   calculator = new SoftMathIterative();
   }
   
   @Test
   public void testMultiply() {
      assertEquals("5 * 6 == 30", 30, calculator.multiply(5, 6));
      assertEquals("1 * 3 == 3", 3, calculator.multiply(1, 3));
      assertEquals("10 * 10 == 100", 100, calculator.multiply(10, 10));
       assertEquals("0 * 6 == 0", 0, calculator.multiply(0, 6));
       assertEquals("6 * 0 == 0", 0, calculator.multiply(6, 0));
      // assertEquals("-2 * 3 == -6", -6, calculator.multiply(-2, 3));
      // assertEquals("2 * -3 == -6", -6, calculator.multiply(2, -3));
      // assertEquals("-2 * -3 == 6", 6, calculator.multiply(2, 3));
       assertEquals("-2 * 0 == 0", 0, calculator.multiply(-2, 0));
       assertEquals("0 * -5 == 0", 0, calculator.multiply(0, -5));
   }
   
   @Test
   public void testRandom() {
      Random generator = new Random();
      int a, b;
      
      for (int i = 0; i < TRIAL_COUNT; ++i) {
         // uncomment to allow negatives and 0
         a = generator.nextInt(100) + 1; // - 50;
         b = generator.nextInt(100) + 1; // - 50;

         assertEquals(a + " * " + b, a * b, calculator.multiply(a, b));
      }  
   }
   
   @Test
   public void testTime() {
      System.out.println("Recursive: " + timeSeconds(new SoftMathRecursive()));
      System.out.println("Iterative: " + timeSeconds(new SoftMathIterative()));
   }
   
   /**
    * Measure the performance of a SoftMath implementation.
    * 
    * @param calc
    * The SoftMath implementation to measure.
    * 
    * @return
    * The number of seconds required to perform a sequence of
    * multiplications.
    */
   public static double timeSeconds(SoftMath calc) {
      Random generator = new Random(10);
      
      ThreadMXBean time = ManagementFactory.getThreadMXBean();
      long start = time.getCurrentThreadCpuTime();

      for (int i = 0; i < TRIAL_COUNT; ++i) {
         calc.multiply(generator.nextInt(101) + 1 /* - 50 */,
                       generator.nextInt(101) + 1 /* - 50 */);
      }
      
      long end = time.getCurrentThreadCpuTime();
      
      return (end - start) / 1.0e9;
   }
}
