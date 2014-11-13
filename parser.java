import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class parser {

 private static void readFile(String fileName) {
   try {
     File file = new File(fileName);
     Scanner scanner = new Scanner(file);

     while (scanner.hasNext()) {
        System.out.println(scanner.next().replaceAll("[.,<>]", "").toLowerCase());
     }
     scanner.close();
   } catch (FileNotFoundException e) {
     e.printStackTrace();
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