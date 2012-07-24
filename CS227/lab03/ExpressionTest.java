package lab03;

public class ExpressionTest {

	public static void main(String[] args) {

		// Our int
		int digits = 50000;
		// Put digits into a string by concatenating it to an empty string
		String digit = "" + digits;
		// Print out the string's length
		System.out.println(digit.length());
		
		// The number of voters we have
		int numVoters = 1000;
		// We multiply numVoters by 2/3 and add .5 and cast it as an int
		int majorityInt = (int) (numVoters * 2 / 3.0 + .5);
		System.out.println(majorityInt);
	}

}
