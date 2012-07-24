package pre2;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

public class UtilitiesTest {

	public static void main(String[] args) {
		
		Color emerald = Utilities.getColor("80 200 120"); 
		System.out.println(emerald.toString()); // output will be: // java.awt.Color[r=80,g=200,b=120] 
		Color royalPurple = Utilities.getColor("6B3FA0"); 
		System.out.println(royalPurple.toString()); // output will be: // java.awt.Color[r=107,g=63,b=160]
		
		JFrame frame = new JFrame();
		frame.setSize(new Dimension(200,200));
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Utilities.getColor("6B3FA0"));
		frame.setVisible(true);
		
	}
	
}
