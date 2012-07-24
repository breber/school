package lab05;

import java.util.Random;

public class Uniform {
	
	public static void main(String[] args)
	{
		int numRolls = 102;

		if (args.length == 1) numRolls = Integer.parseInt(args[0]);

		Random rand = new Random();
		for (int i = 0; i < numRolls; i++)
		{
			System.out.println(rand.nextInt(6)+1);
		}
	}

}