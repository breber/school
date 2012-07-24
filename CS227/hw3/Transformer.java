package hw3;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Reads in a PPM in from a file, applies 3 effects,
 * and outputs the result to a file.
 * 
 * @author Brian Reber
 */
public class Transformer {
	/**
	 * The main method.
	 * 
	 * @param args
	 * -First argument = the file path to the PPM file to read in
	 * -Second argument = the path for the output file
	 */
	public static void main(String[] args) {
		Image img = new Image();
		
		//Load the image from file
		try {
			img.load(args[0]);
		} catch (FileNotFoundException e) {
			System.out.println("Load failed! File not found!");
		}

		//Apply my transform & two others
		img.addTransform(new MyEffect());
		img.addTransform(new OtherEffect1());
		img.addTransform(new OtherEffect2());
		img.transform();
		
		//Write the result to file
		try {
			img.writeTo(args[1]);
		} catch (IOException e) {
			System.out.println("There was an IOException in writing the file.");
		}
	}
}
