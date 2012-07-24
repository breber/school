package lab05;

import java.util.Random;

public class Loaded {
	
	public static void main(String[] args)
	{
		int numRolls = 102;

		if (args.length == 1) numRolls = Integer.parseInt(args[0]);

		Random rand = new Random();
		for (int i = 0; i < numRolls; i++)
		{
			int j = rand.nextInt(6) + 1;
			if (j == 2 && i != (numRolls -1)) 
			{
				System.out.println(j);
				System.out.println(j);
				i++;
			}
			else System.out.println(j);
		}
	}

}