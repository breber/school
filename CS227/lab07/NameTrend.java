package lab07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class NameTrend {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner file = new Scanner(new File("src/lab07/top10.txt"));
		int year = 0;

		while (file.hasNextLine())
		{
			String line = file.nextLine();
			Scanner line1 = new Scanner(line);

			if (line1.hasNextInt())
			{
				year = line1.nextInt();
			} 
			else
			{
				while (line1.hasNext())
				{
					if (line1.next().equals("Robert"))
					{
						System.out.println(year + " " + line1.nextDouble());
					}
				}
			}
		}

		file.close();
	}

}
