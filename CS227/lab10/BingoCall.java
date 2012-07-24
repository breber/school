package lab10;

/**
 * @author brianreber
 *
 */
public class BingoCall {
	@SuppressWarnings("unused")
	private char letter;
	@SuppressWarnings("unused")
	private int number;

	public BingoCall(char givenLetter,
			int givenNumber) {
		letter = givenLetter;
		number = givenNumber;
	}

	public static void main(String[] args) {
		BingoCall call = new BingoCall('B', 7);
		String representation = call.toString();
		System.out.println(representation);
		System.out.println(call);
	}
}

