package lab07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MileageCalculator {


	public static void main(String[] args) throws FileNotFoundException {

		Scanner file = new Scanner(new File("src/lab07/car.txt"));
		int numLines = file.nextInt();
		
		double[] mileage = new double[numLines];
		double[] gallons = new double[numLines];
		
		for (int i = 0; i < numLines; i++)
		{
			mileage[i] = file.nextDouble();
			gallons[i] = file.nextDouble();
		}
		
		for (int i = 1; i < numLines; i++)
		{
			double miles = (mileage[i] - mileage[i-1]) / gallons[i];
			System.out.println("Mileage " + i + ": " + miles);
		}
		
	}

}
