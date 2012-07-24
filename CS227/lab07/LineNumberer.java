package lab07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class LineNumberer {

	public static void main(String[] args) throws FileNotFoundException {

		Scanner input = new Scanner(new File("src/lab07/LineNumberer.java"));
		int lineCount = 1;

		while (input.hasNextLine()) {
			System.out.print(lineCount);
			System.out.println(" " + input.nextLine());
			lineCount++;
		}

		input.close();
	}
}
