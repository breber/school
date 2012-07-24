package hw2;

public class BinaryTester {

	public static void main(String[] args) {
//		long n = Long.valueOf("59");
//		long n1 = 3;
		
		String n = "110111100000101101101011001110100111011001000000000000000000";
//		String n1 ="1000101011000111001000110000010010001001111010000000000000000000";
		
//		String n = "1101";
//		String n1 ="10";
		
//		System.out.println("       " + n);
		
		BinaryNumber num = new BinaryNumber(n);
//		BinaryNumber num1 = new BinaryNumber(n1);
	
		System.out.println("Num  = " + num.toDecimal() + " which has " + String.valueOf(num.toDecimal()).length() + " chars");
		System.out.println("Num  = " + num);
//		System.out.println("Num1 = " + num1.toDecimal()+ " which has " + String.valueOf(num1.toDecimal()).length() + " chars");
//		System.out.println("Num1 = " + num1);
		
//		System.out.println("Mult = " + num.times(num1));
//		System.out.println("Divi = " + num.divide(num1));
//		System.out.println("Mod  = " + num.modulus(num1));
//		System.out.println("Add  = " + num.add(num1));
//		System.out.println("Sub  = " + num.difference(num1));

	}
	
}
