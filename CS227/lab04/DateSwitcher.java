package lab04;

import java.util.Scanner;

public class DateSwitcher {

	public static void main(String[] args) {
		String date = "2/7/2010";
		
		System.out.println(date);
		
		Scanner input = new Scanner(date);
		input.useDelimiter("/");
		
		if (date.contains("/"))
		{
			int day = input.nextInt();
			int month = input.nextInt();
			int year = input.nextInt();
			
			System.out.println(year + "/" + month + "/" + day);
		}
		else System.err.println("Incorrect date format");
	}
	
}
