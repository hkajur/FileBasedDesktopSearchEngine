import java.io.*;
import java.util.*;

public class FDSEE {
    // isSearchable()

    public enum SETF {
        HTML,
        HTM,
        TXT,
        CC,
        CPP,
        C,
        H,
        JAVA
    }

    public static void isSearchable(String filename){
        
        File file = new File(filename);

        boolean isFileExists = fileExists(file);

        if(isFileExists){
            System.out.println("File exists");
        } else {
            System.out.println("File doesn't exist");
        }
    }

    public static boolean fileExists(File f){
        if(f.exists())
            return true;
        return false;
    }

    public static void main(String[] args){
        
        try {

            if(args.length == 0 || args.length == 1){
                System.err.println("Error: Need at least two argument");
                System.exit(1);
            }
            
            if(args.length > 2){
                System.err.println("Error: Can't have more than two argument");
                System.exit(1);
            }

            String type = args[0];
            
            String filename = args[1];

            switch(type){
                case "searchable":
                    isSearchable(filename);
                    break;
                default:
                    break;
            }

        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
