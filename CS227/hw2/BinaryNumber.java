package hw2;

/**
 * An immutable class representing a Binary Number
 * 
 * @author brianreber
 */
public class BinaryNumber {

	/**
	 * The string representing this binary number instance
	 */
	private final String binNum;
	
	/**
	 * A class constant because adding 1 is a common operation, so this makes it more
	 * reliable and consistent
	 */
	private static final BinaryNumber ONE = new BinaryNumber("1");
	
	/**
	 * Defined for use with compareTo to make it more readable
	 */
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	private static final int LESS = -1;

	/**
	 * Create a new BinaryNumber by passing the given int into
	 * the long constructor.
	 * 
	 * @param n An integer in base 10
	 */
	public BinaryNumber(int n)
	{
		// Just pass the int into the long constructor to make life easier
		this((long) n);
	}

	/**
	 * Create a new BinaryNumber from the given long
	 * 
	 * @param n A long in base 10
	 */
	public BinaryNumber(long n)
	{
		// If n = 0, then we don't need to go through the loops
		if (n == 0) binNum = "0";
		
		else {
			String tempBin = "";

			long tempN = n;

			// Mod and divide by 2 to get the binary representation
			while (tempN > 0)
			{	
				if ((tempN % 2) == 1)
					tempBin += "1";
				else tempBin += "0";
				
				tempN /= 2;
			}
			binNum = reverseString(tempBin);
		}
	}

	/**
	 * Create a new BinaryNumber from the given string of 
	 * 0's and 1's.
	 * 
	 * @param val A String of 0's and 1's
	 */
	public BinaryNumber(String val)
	{
		binNum = val;
	}

	/**
	 * Sums this BinaryNumber with the given BinaryNumber
	 * 
	 * @param other BinaryNumber object to add to
	 * @return The sum of this BinaryNumber and other 
	 */
	public BinaryNumber add(BinaryNumber other)
	{
		String newNum = "";
		// Reverse the strings to make the loops easier
		// (forward instead of backwards)
		String otherStr = reverseString(other.toString());
		String thisString = reverseString(binNum);
		int maxLoop;
		int carry = 0;

		// Set the loop to go until the length of the longest string
		if (otherStr.length() > thisString.length()) maxLoop = otherStr.length();
		else maxLoop = thisString.length();		

		// Iterate through and add each place
		for (int i = 0; i < maxLoop; i++)
		{
			// Need to find the sum of the values in each column
			int sum = 0;

			// Check to see if there is a 1 at the current position, as well as
			// whether the loop iterator is still within the string's length.  
			// Also add the carry value
			if (i < thisString.length() && thisString.charAt(i) == '1') sum++;
			if (i < otherStr.length() && otherStr.charAt(i) == '1') sum++;
			sum += carry;

			// Figure out what to do - if the sum of the digits at the current position is
			// 3, put a 1 in the new string, and carry 1.
			if (sum == 3) {
				newNum += "1";
				carry = 1;
				// If on the last digit and need to carry, just do that here
				if (i == maxLoop - 1) newNum += "1";
			} else if (sum == 2) {
				newNum += "0";
				carry = 1;
				if (i == maxLoop - 1) newNum += "1";
			} else if (sum == 1) {
				newNum += "1";
				carry = 0;
			} else if (sum == 0) {
				newNum += "0";
				carry = 0;
			}
		}

		return new BinaryNumber(reverseString(newNum));
	}

	/**
	 * Multiplies this BinaryNumber by the other one
	 * 
	 * @param other BinaryNumber object to multiply with
	 * @return The product of this BinaryNumber and other
	 */
	public BinaryNumber times(BinaryNumber other)
	{
		//Create a new BinaryNumber set to 0
		BinaryNumber returnVal = new BinaryNumber(0);

		// Reverse the strings for ease of use in the loop
		String thisRev = reverseString(binNum);
		String otherRev = reverseString(other.toString());

		/*	1011
		 * x1010
		 * -----
		 * 1101110 
		 */

		// Go through all the numbers in the bottom row
		for (int i = 0; i < otherRev.length(); i++)
		{
			String temp = "";
			int tempOtherChar = Integer.parseInt(otherRev.substring(i,i+1));

			/* 
			 * Then go through every number in the top row and 
			 * multiply it by the current number in the bottom row. 
			 */
			for (int j = 0; j < thisRev.length(); j++)
			{
				int tempThisChar = Integer.parseInt(thisRev.substring(j,j+1));
				temp += (tempOtherChar * tempThisChar);
			}

			// Reverse the string back
			temp = reverseString(temp);

			// And add it to the previous return value after shifting it i places
			returnVal = returnVal.add(new BinaryNumber(temp).shift(i));
		}

		return returnVal;
	}

	/**
	 * Finds the difference between this BinaryNumber and the
	 * given one.
	 * 
	 * @param other BinaryNumber object to subtract
	 * @return The difference of this BinaryNumber and other. The result is always non-negative.
	 */
	public BinaryNumber difference(BinaryNumber other)
	{		
		BinaryNumber thisNum;

		String otherStr;
		String thisString;

		// Check which one is bigger. When subtracting binary numbers, it doesn't matter which
		// order they go in, so for simplicity we will subtract the smaller from bigger
		if (other.compareTo(this) != GREATER)
		{
			thisNum = new BinaryNumber(this.toString());
			// Reverse the other strings so we can easily add zeros to the "front" to
			// make the "not and add" trick work
			otherStr = reverseString(other.toString());//11101
			thisString = this.toString();
		} else {
			thisNum = new BinaryNumber(other.toString());
			otherStr = reverseString(this.toString());//11101
			thisString = other.toString();
		}
		// Need to make the numbers the same length by adding 0's to
		// the right (technically the left, but we reversed the string)
		int dif = thisString.length() - otherStr.length();
		for (int i = 0; i < dif; i++)
		{
			otherStr += "0";//1110100
		}
		// Reverse it back
		otherStr = reverseString(otherStr);//0010111
		// and make a new BinaryNumber
		BinaryNumber temp = new BinaryNumber(otherStr);
		// "not" it, and add 1
		temp = temp.not().add(ONE);//1101001

		// Since we aren't sure which one is going to be added, we
		// created a new BinaryNumber and added on to that one.
		temp = thisNum.add(temp);

		return new BinaryNumber(temp.toString().substring(1));
	}

	/**
	 * Divides this number by the given BinaryNumber
	 * 
	 * @param other BinaryNumber object to divide
	 * @return The quotient of the division with the remainder discarded
	 */
	public BinaryNumber divide(BinaryNumber other)
	{
		// Start by creating BinaryNumbers with clear var names for
		// use in division
		BinaryNumber dividend = new BinaryNumber(this.toString());
		BinaryNumber divisor = other;

		// Used to know how far the loop is in the original dividend
		int length = dividend.toString().length();

		// The new value to return
		String quotient = "";

		// The value we start searching at after subtraction
		int startSearch = 1;

		while (length > 0)
		{			
			if (startSearch > dividend.toStringWithLeadingZeros().length()) break;
			// if the current divisor is greater than the dividend up to the current (i) character
			// Leading zeros are left on the dividend to keep the counters correct
			if (divisor.compareTo(new BinaryNumber(dividend.toStringWithLeadingZeros().substring(0, startSearch))) == GREATER)
			{
				// we add a 0 to the return string
				quotient += "0";
				length--;
			} else {
				// Otherwise we add a 1, and then subtract the divisor from the current dividend up to
				// the current (i) character
				quotient += "1";
				BinaryNumber temp = new BinaryNumber(dividend.toString().substring(0, startSearch));
				temp = temp.difference(divisor);
				temp = new BinaryNumber(temp.toString());

				// If our temp BinaryNumber doesn't have a 1, we don't want to add it
				// to our dividend.  It will mess up the counters
				if (!temp.toString().contains("1"))
					dividend = new BinaryNumber(dividend.toString().substring(startSearch));
				else
					dividend = new BinaryNumber(temp.toString() + dividend.toString().substring(startSearch));
				
				// We want to start the next round at the value after the subtraction, plus
				// the one we "dropped" down.
				if (!temp.toString().contains("1"))
					startSearch = 0;
				else
					startSearch = temp.toString().length();
				length--;
			}
			startSearch++;
		}
		// We will return a new BinaryNumber containing the quotient with leading zeros removed
		return new BinaryNumber(removeLeadingZeros(quotient));
	}

	/**
	 * Finds the modulus of the given BinaryNumber and this BinaryNumber
	 * 
	 * @param other BinaryNumber object to mod
	 * @return The remainder of the division.
	 */
	public BinaryNumber modulus(BinaryNumber other)
	{
		// We start by creating BinaryNumbers with clear var names for
		// use in division
		BinaryNumber dividend = new BinaryNumber(this.toString());
		BinaryNumber divisor = other;

		// Used to know how far we are in the original dividend
		int length = dividend.toString().length();

		// The value we start searching at after subtraction
		int startSearch = 1;

		while (length > 0)
		{			
			if (startSearch > dividend.toStringWithLeadingZeros().length()) break;
			// if the current divisor is greater than the dividend up to the current (i) character
			if (divisor.compareTo(new BinaryNumber(dividend.toStringWithLeadingZeros().substring(0, startSearch))) == GREATER)
			{
				length--;
			} else {
				BinaryNumber temp = new BinaryNumber(dividend.toString().substring(0, startSearch));
				temp = temp.difference(divisor);
				temp = new BinaryNumber(removeLeadingZeros(temp.toString()));

				dividend = new BinaryNumber(removeLeadingZeros(temp.toString() + dividend.toString().substring(startSearch)));

				// We want to start the next round at the value after the subtraction, plus
				// the one we "dropped" down.
				if (!temp.toString().contains("1"))
					startSearch = 0;
				else
					startSearch = temp.toString().length();
				length--;
			}
			// Increment so that we can continue our search
			startSearch++;
		}

		// Once we get through the length of the original dividend, we will return the remainder
		return new BinaryNumber(removeLeadingZeros(dividend.toString()));
	}

	/**
	 * Finds the bitwise NOT of this BinaryNumber
	 * 
	 * @return The bitwise NOT of this BinaryNumber, which flips all the bits
	 */
	public BinaryNumber not()
	{
		String newBin = "";
		int length = this.length();

		String otherStr = this.toString();

		// Since we use the toString method, we need to keep track of how many
		// leading zeros there were, and add them on manually.  This is because
		// the toString method is set to return it without leading zeros.
		otherStr = reverseString(otherStr);
		int dif = length - otherStr.length();
		for (int i = 0; i < dif; i++)
		{
			otherStr += "0";//1110100
		}

		otherStr = reverseString(otherStr);
		for (int i = 0; i < otherStr.length(); i++)
		{
			if (otherStr.charAt(i) == '0') newBin += '1';
			else newBin += '0';
		}
		return new BinaryNumber(newBin);
	}

	/**
	 * Finds the minimum of this BinaryNumber and the given one
	 * 
	 * @param other BinaryNumber object to find the minimum with
	 * @return The minimum of this BinaryNumber and other.
	 */
	public BinaryNumber min(BinaryNumber other)
	{
		if (compareTo(other) == LESS)
			return this;

		else return other;
	}

	/**
	 * Finds the maximum of this BinaryNumber and the given one
	 * 
	 * @param other BinaryNumber object to find the max with
	 * @return The maximum of this BinaryNumber and other.
	 */
	public BinaryNumber max(BinaryNumber other)
	{
		if (compareTo(other) == GREATER)
			return this;

		else return other;
	}

	/**
	 * Compares this BinaryNumber to the other
	 * 
	 * @param other BinaryNumber object to compare to
	 * @return
	 *  - 0 if the two have the same number<br />
	 *  - Positive if this B.N. has a higher value than other<br />
	 *  - Negative if this B.N. has a lower value than other
	 */
	public int compareTo(BinaryNumber other)
	{
		// We start by removing the leading zeros
		String otherStr = removeLeadingZeros(other.toString());
		String thisStr = removeLeadingZeros(binNum);

		// Then we check the lengths of the
		// two strings.  Easiest way to tell if one
		// is greater than the other.
		if (otherStr.length() > thisStr.length())
		{
			return LESS;
		} else if (otherStr.length() < thisStr.length()) {
			return GREATER;
		}

		/* If they have the same length, we will go through 
		 * character by character and compare them
		 */
		for (int i = 0; i < otherStr.length(); i++)
		{
			if (otherStr.charAt(i) == '1' && thisStr.charAt(i) == '0')
				return LESS;
			else if (otherStr.charAt(i) == '0' && thisStr.charAt(i) == '1')
				return GREATER;
		}

		/* If all the characters are the same we will end up
		 * here and return 0 meaning they are equal. 
		 */
		return EQUAL;
	}

	/**
	 * Shifts the values to the left (adds n 0's to the end)
	 * 
	 * @param n Number of places to shift
	 * @return New BinaryNumber number with n trailing zeros added
	 */
	public BinaryNumber shift(int n)
	{
		String tempBinary = binNum;
		for (int i = 0; i < n; i++)
		{
			tempBinary += "0";
		}
		return new BinaryNumber(tempBinary);
	}

	/**
	 * Finds the number of bits in this BinaryNumber
	 * 
	 * @return The number of bits in this BinaryNumber without leading zeros
	 */
	public int length()
	{
		return binNum.length();
	}

	/**
	 * Override the default toString method to make it return
	 * this binary number instance without the leading zeros.
	 */
	@Override
	public String toString()
	{
		return removeLeadingZeros(binNum);
	}

	/**
	 * Gives us the binary number with leading zeros in case we need
	 * the zeros.
	 * 
	 * @return the Binary Number with all leading zeros.
	 */
	private String toStringWithLeadingZeros()
	{
		return binNum;
	}

	/**
	 * Converts the BinaryNumber back to decimal
	 * 
	 * @return String representation of this BinaryNumber as an unsigned integer in base 10
	 */
	public String toDecimal()
	{
		String retVal = "";

		BinaryNumber[] nums = new BinaryNumber[10];

		// We fill an array for our "key" to compare
		// values to
		for (int i = 0; i < nums.length; i++)
		{
			nums[i] = new BinaryNumber(i);
		}

		BinaryNumber x = new BinaryNumber(binNum);

		// Start at the end, and work our way backwards,
		// modding and dividing by 10 to get the decimal representation
		while (x.compareTo(new BinaryNumber(0)) == GREATER)
		{
			BinaryNumber modVal = x.modulus(new BinaryNumber(10));
			for (int i = 0; i < nums.length; i++)
			{
				if (nums[i].compareTo(modVal) == EQUAL)
				{
					retVal += i;
					break;
				}
			}
			x = x.divide(new BinaryNumber(10));
		}
		return reverseString(retVal);
	}

	/**
	 * Overloaded so we can pass in groups of 4 to
	 * use it in toHexadecimal().  A group of 4 in binary
	 * represents one hex character, which means we can easily
	 * convert each set of 4 binary digits into one
	 * hex character.
	 * 
	 * @param num Binary String to find decimal value of
	 * @return The integer value of the binary represented in the
	 * parameter num.
	 */
	private int toDecimal(String num)
	{
		int dec = 0;

		for (int i = 0; i < num.length(); i++)
		{
			if (num.charAt(i) == '1')
				dec += Math.pow(2, i);
		}

		return dec;
	}

	/**
	 * Converts this BinaryNumber to Hex
	 * 
	 * @return String representation of this BinaryNumber as an unsigned integer in base 16
	 */
	public String toHexadecimal()
	{
		String hex = "";
		String binRev = reverseString(binNum);

		/* We go through and convert every group of 4 numbers
		 * into decimal, and from there we can concatenate the 
		 * result onto our string.
		 */
		for (int i = 0; i < binNum.length(); i += 4)
		{
			int hexNum = 0;

			if (binNum.length() - i >= 4)
			{
				hexNum = toDecimal(binRev.substring(i, i + 4));
			}
			else hexNum = toDecimal(binRev.substring(i));

			if (hexNum == 10) hex += "A";
			else if (hexNum == 11) hex += "B";
			else if (hexNum == 12) hex += "C";
			else if (hexNum == 13) hex += "D";
			else if (hexNum == 14) hex += "E";
			else if (hexNum == 15) hex += "F";
			else hex += hexNum;
		}

		return reverseString(hex);
	}

	/**
	 * Reverses a given string to make the loops easier
	 * 
	 * @param str String to reverse
	 * @return str reversed (1101 --> 1011)
	 */
	private String reverseString(String str)
	{
		String newString = "";
		for (int i = str.length()-1; i >= 0; i--)
		{
			newString += str.charAt(i);
		}

		return newString;
	}

	/**
	 * Removes the leading zeros from a String
	 * 
	 * @param str String of 0's and 1's to remove the leading 0's from
	 * @return str without any leading zeros
	 */
	private String removeLeadingZeros(String str)
	{
		// If it doesn't contain any 1's, we will just return 0
		if (!str.contains("1"))
			return "0";

		// otherwise we scan through until we hit a nonzero term
		for (int i = 0; i < str.length(); i++)
		{
			// if it is nonzero, we return from the current position
			// to the end of the number
			if (str.charAt(i) != '0')
			{
				return str.substring(i);
			}
		}

		return str;
	}
}