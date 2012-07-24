package lab05;

import java.util.Scanner;

public class FairDieTester {
	
	public static void main(String[] args)
	{
		int one = 0, two = 0, three = 0, four = 0, five = 0, six = 0;
		int totalCounted = 0;

		Scanner scan = new Scanner(System.in);

		while (scan.hasNextInt())
		{
			int num = scan.nextInt();
			if (num == 1) one++;
			else if (num == 2) two++;
			else if (num == 3) three++;
			else if (num == 4) four++;
			else if (num == 5) five++;
			else if (num == 6) six++;
			totalCounted++;
		}

		System.out.println("1 = " + one);
		System.out.println("2 = " + two);
		System.out.println("3 = " + three);
		System.out.println("4 = " + four);
		System.out.println("5 = " + five);
		System.out.println("6 = " + six);
		System.out.println("Total Rolls = " + totalCounted);
	}

}