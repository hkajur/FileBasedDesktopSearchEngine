/*
 * Harish Kajur
 * Jonathan Lysiak 4477
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class parser {

 private static void readFile(String fileName) {
   try {
     File file = new File(fileName);
     Scanner scanner = new Scanner(file);

     while (scanner.hasNext()) {
        String s = scanner.next().replaceAll("[^A-Za-z0-9]", " ").toLowerCase();

        s = s.replaceAll("[ ][ ]*", " ");
        s = s.replaceAll("^[ ]*", "");

        String[] strArray = s.split(" ");

        for(String str : strArray)
          if(!str.isEmpty() && str.trim().length() > 0)
            System.out.println(str);
     }
     scanner.close();
   } catch (FileNotFoundException e) {
     e.printStackTrace(); //Prints errors if file not found
   }
 }

 public static void main(String[] args) {
   if (args.length != 1) {
     System.err.println("usage: java TextScanner1"
       + "file location");
     System.exit(0);
   }
   readFile(args[0]);
 }
}