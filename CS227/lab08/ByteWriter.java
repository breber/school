/**
 * 
 */
package lab08;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;


/**
 * 
 * @author brianreber
 *
 */
public class ByteWriter {

   public static void main(String[] args) throws FileNotFoundException,
                                                 IOException {

      FileOutputStream out = new FileOutputStream("src/lab08/binaryfile1.bin");
      
      Scanner in = new Scanner(System.in);

      System.out.print("Enter a byte: ");
      while (in.hasNextByte()) {
         out.write(in.nextByte());
         System.out.print("Enter a byte: ");
      }

      out.close();
   }
}
