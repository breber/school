package lab06;

public class WordCross {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		printCross("horoscope", "vacuum");		
		printCross("brian", "reber");
	}

	/**
	 * Checks to see if there are common letters in the two words, and
	 * prints them out intersecting if there is a match.  Otherwise
	 * we will let the user know there are no matches.
	 * 
	 * @param horiz
	 * @param vert
	 */
	public static void printCross(String horiz, String vert)
	{
		boolean shared = false;
		int sharedCharIndexVert = 0, sharedCharIndexHoriz = 0;
		
		//Start searching for matches
		for (int i = 0; i < horiz.length(); i++)
		{
			//if vertical contains the character at the current horizontal position
			if (vert.contains(horiz.substring(i, i+1)))
			{
				//we keep the index of vertical position as well as horizontal position
				sharedCharIndexVert = vert.indexOf(horiz.charAt(i));
				sharedCharIndexHoriz = i;
				shared = true;
				break;
			}
		}
		//If we didn't fine a match, we will print that out, and return to the calling method
		if (!shared) 
		{
			System.out.println("These words do not cross");
			return;
		}

		//We go through the vertical positions
		for (int j = 0; j < vert.length(); j++)
		{
			//if this isn't the intersecting row
			if (j != (sharedCharIndexVert))
			{
				//we will print out enough spaces
				for (int k = 0; k < sharedCharIndexHoriz; k++)
					System.out.print(" ");
				//and the vertical letter at that position
				System.out.println(vert.charAt(j));
			}
			//if it is the intersecting row, we will print out the horiz word
			else System.out.println(horiz);
		}

		System.out.println("These words have " + horiz.charAt(sharedCharIndexHoriz) + " in common.");
	}
}
