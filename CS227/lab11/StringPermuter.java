package lab11;

/**
 * @author brianreber
 *
 */
public class StringPermuter {

	public static void main(String[] args) {
		permute("abcd");
	}

	/**
	 * Generate all permutations of the characters in the
	 * specified String.
	 * 
	 * @param toPermute
	 * The String whose characters will be permuted.
	 */
	public static void permute(String toPermute) {
		permute("", toPermute);
	}

	/**
	 * Recursive helper method to generate all permutations of
	 * the characters in the toPermute.
	 * 
	 * @param permuted
	 * The characters that have already been permuted.
	 * 
	 * @param toPermute
	 * The remaining characters that have yet to be permuted.
	 */
	private static void permute(String permuted,
			String toPermute) {

		// Base case. If there are no characters left to permute,
		// let's print this permutation of characters.
		if (toPermute.length() == 0) {
			System.out.println(permuted);
		}

		// General case. For each of the unpermuted characters, we add
		// it the String of already permuted characters and permute the
		// remaining characters.
		else {
			for (int i = 0; i < toPermute.length(); ++i) {
				permute(permuted + toPermute.charAt(i),
						toPermute.substring(0, i) + toPermute.substring(i + 1));
			}
		}
	}
}
