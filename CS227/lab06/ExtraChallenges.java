package lab06;

public class ExtraChallenges {

	public static void main(String[] args) {
		System.out.println(getShorthand("computerscience"));
		
		printSquare(4);
	}
	
	public static String getShorthand(String word)
	{
		String shortened = "";
		
		for (int i = 0; i < word.length(); i++)
		{
			char c = word.charAt(i);
			if (!(c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u'))
				shortened += c;
		}
		
		return shortened;
	}
	
	public static void printSquare(int num)
	{
		for (int i = 0; i < num; i++)
		{
			for (int j = 0; j < num; j++)
			{
				if ((j == 0 || j == (num-1)) || (i == 0 || i == (num-1)))
					System.out.print("*");
				else System.out.print(" ");
			}
			System.out.println();
		}
	}
}
