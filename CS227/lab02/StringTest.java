package lab02;

public class StringTest {

	public static void main(String[] args) {

		String message;
		message = new String("Hello, world!");
		System.out.println(message);
		
		System.out.println(message.length());
		
		System.out.println(message.toUpperCase());
		System.out.println(message.substring(7));
		System.out.println(message.replace('!','?'));
		
		String emptyString = new String();
		
		System.out.println(emptyString.isEmpty());
	}

}
