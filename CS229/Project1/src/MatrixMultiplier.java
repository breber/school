import java.util.Scanner;

/**
 * Reads in 2 3x3 matrices, multiplies them in a C function, and
 * puts the result in the out array.
 *
 * @author Brian Reber
 */
public class MatrixMultiplier {

	/**
	 *	Multiplies 2 3x3 matricies
	 *
	 * @param arr1 - the first 3x3 matrix
	 * @param arr2 - the second 3x3 matrix
	 * @param arr3 - the output 3x3 matrix
	 */
	public native void multiply(int arr1[][], int arr2[][], int arr3[][]);

	static {
		System.loadLibrary("matrix");
	}

	public static void main(String[] args) {
		int count = 0;
		int[][] arr1 = new int[3][3];
		int[][] arr2 = new int[3][3];
		int[][] out = new int[3][3];
		
		Scanner scan = new Scanner(System.in);
		
		/*
		 * Reads in 18 ints (2 3x3 arrays) to the two input arrays.
		 * The input can be formatted by rows, or all on one line. As long as the data is
		 * whitespace-delimited, it should be read properly.
		 */
		while (scan.hasNextInt() && count < 18) {
			if (count < 9) {
				arr1[count / 3][count % 3] = scan.nextInt();
			} else {
				arr2[(count - 9) / 3][(count - 9) % 3] = scan.nextInt();
			}
			count++;
		}
		
		// Perform the multiplication
		new MatrixMultiplier().multiply(arr1, arr2, out);
		
		// Print out the output matrix
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				System.out.print(out[i][j] + " ");
			}
			System.out.println();
		}
	}
}
