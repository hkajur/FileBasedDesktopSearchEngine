import java.io.*;
import java.util.*;

public class FDSEE {

    public enum FileType {
        HTML,
        TEXT
    }
    
    public static FileType getFileType(String filename){
        
        filename = filename.replaceFirst("[a-zA-Z0-9$/_]*.", "");
       
        FileType filetype = null;

        switch(filename){
            case "html":
            case "htm":
                filetype = FileType.HTML;
                break;
            case "txt":
            case "cc":
            case "cpp":
            case "java":
            case "c":
            case "h":
                filetype = FileType.TEXT;
                break;
            default:
                break;
        }

        return filetype;
    }
    
    public static void isSearchable(String filename){
        
        File file = new File(filename);

        boolean isFileExists = fileExists(file);

        if(isFileExists){
            
            boolean isFile = file.isFile();
            boolean isDirectory = file.isDirectory();

            if(isFile){
                
                FileType filetype = getFileType(filename);
                
                if(filetype != null){
                    System.out.println(file + "\t" + filetype);
                }

            } else if(isDirectory){

            }
        }

        System.out.println("END-OF-LISTING");
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
