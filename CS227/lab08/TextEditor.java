package lab08;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class TextEditor {
	public static void main(String[] args) throws FileNotFoundException {
		Scanner in = new Scanner(System.in);
		PrintWriter out = new PrintWriter("/Users/brianreber/mydocument.txt");

		// Echo keyboard input out to the file.
		while (in.hasNextLine()) {
			out.println(in.nextLine());
		} 

		out.close();
	}
}
