package lab04;

import java.util.Scanner;

public class ScannerTest {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		
		System.out.print("Please enter a name: ");
		String name = input.next();
		System.out.print("Please enter a number: ");
		int numKids = input.nextInt();
		System.out.print("Please enter a boolean: ");
		boolean trueFalse = input.nextBoolean();
		System.out.print("Please enter a name: ");
		String myName = input.next();
		
		System.out.println("Dear " + name +",\n");
		System.out.println("Wow, what a year. I hope your " + numKids);
		if (numKids == 1)
			System.out.print("kid is ");
		else System.out.print("kids are ");
		
		System.out.println("well. The rumors that we are expecting");
		System.out.println("another are " + trueFalse + ". Until next year!\n");
		
		System.out.println("Sincerely,\n");
		System.out.println(myName);
		
	}

}
