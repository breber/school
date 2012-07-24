/**
 * 
 */
package lab08;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 
 * @author brianreber
 *
 */
public class ByteReader {

   public static void main(String[] args) throws FileNotFoundException,
                                                 IOException {

      FileInputStream in = new FileInputStream("src/lab08/binaryfile.bin");

      int datum = in.read();
      while (datum != -1) {
         System.out.println(datum);
         datum = in.read();
      }

      in.close();
   }
}