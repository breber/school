package lab04;

import java.util.Random;
import java.util.Scanner;

public class NumberPower {


	public static void main(String[] args) {
		int num1, num2;
		
		num1 = getNumber();
		num2 = getNumber();
		
		System.out.println(num1 + " ^ " + num2 + " = " + Math.pow(num1, num2));
	}

	private static int getNumber()
	{
		Random rand = new Random();
		Scanner scan = new Scanner(System.in);

		System.out.println("Enter a number or a non-number: ");
		if (scan.hasNextInt())
			return scan.nextInt();
		else return rand.nextInt(6);

	}
	
}
