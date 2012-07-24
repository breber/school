package lab05;

import java.util.Scanner;

public class Upper {

   public static void main(String[] args) {

      Scanner input = new Scanner(System.in);
      while (input.hasNextLine()) {
         System.out.println(input.nextLine().toUpperCase());
      }
   }
}