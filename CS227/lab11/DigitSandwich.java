package lab11;

/**
 * @author brianreber
 *
 */
public class DigitSandwich {

	public static void main(String[] args) {
		System.out.println(getDigitSandwich(9));
	}

	/**
	 * Compose a digit sandwich, in which the outermost layer is the
	 * specified digit and the inner layers decrease by 1 until the
	 * 0 at the center of the sandwich is reached.
	 * 
	 * @param digitToAdd
	 * Number to add as the outermost layer of the sandwich.
	 * 
	 * @return
	 * The digit sandwich.
	 */
	public static String getDigitSandwich(int digitToAdd) {

		String sandwich;

		if (digitToAdd == 0) {
			sandwich = "" + 0;
		} else {
			sandwich = digitToAdd + getDigitSandwich(digitToAdd - 1) + digitToAdd;
		}

		return sandwich;
	}
}
