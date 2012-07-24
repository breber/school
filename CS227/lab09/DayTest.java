package lab09;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Scanner;

import org.junit.Before;
import org.junit.Test;

/**
 * @author brianreber
 *
 */
public class DayTest {

	private ArrayList<String> data = new ArrayList<String>();
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		data.add("October 19 th");
		data.add("March 3 rd");
		data.add("January 2 nd");
		data.add("September 1 st");
		data.add("August 29 th");
		data.add("October 15 th");
		data.add("December 2 nd");
	}

	/**
	 * Test method for {@link lab09.Day#toString()}.
	 */
	@Test
	public void testToString() {
		ArrayList<Day> d = new ArrayList<Day>();
		
		//We build our ArrayList of type Day from the corresponding elements of our 
		//ArrayList of type String
		for (String s:data)
		{
			Scanner scan = new Scanner(s);
			d.add(new Day(scan.next(), scan.nextInt()));
		}
		
		for (int i = 0; i < d.size(); i++)
		{
			//We get the element out of our string ArrayList
			Scanner scan = new Scanner(data.get(i));
			
			//Build our expected string
			String expected = scan.next() + " " + scan.next() + "" + scan.next();
			
			//And compare to the toString() method
			assertTrue(d.get(i).toString(),  d.get(i).toString().equals(expected));
		}	
	}

}
