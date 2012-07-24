package lab06;

public class DebugTest1 {

	/**
	 * A message.
	 */
	private static String message = "foo";

	public static void main(String[] args) {
		setMessage();
		System.out.println(message);
	}

	/**
	 * Set message to xj9000.
	 */
	public static void setMessage() {
		String message = "xj";
		message += 9000;
	}
}

