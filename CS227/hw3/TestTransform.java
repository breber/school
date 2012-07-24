package hw3;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Reads in a PPM in from a file, applies 3 effects,
 * and outputs the result to a file.
 * 
 * @author Brian Reber
 */
public class TestTransform {
	/**
	 * The main method.
	 * 
	 * @param args
	 * -First argument = the file path to the PPM file to read in
	 * -Second argument = the path for the output file
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException {
		RandomPPMGen img = new RandomPPMGen(25,30);
		
		img.fill();
		
//		Image img = new Image();
//		img.load("src/hw3/test.ppm");
		
		try {
			img.writeTo("/Users/brianreber/Desktop/out1orig.ppm");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		img.crop(5, 0, 10, 16);
		img.rotate();
		
		//Apply my transform & two others
		img.addTransform(new MyEffect());
		img.addTransform(new OtherEffect1());
		img.addTransform(new OtherEffect2());
		img.transform();
		
		//Write the result to file
		try {
			img.writeTo("/Users/brianreber/Desktop/out1.ppm");
		} catch (IOException e) {
			System.out.println("There was an IOException in writing the file.");
		}
	}
}
