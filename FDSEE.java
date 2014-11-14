import java.io.*;
import java.io.*;
import java.util.*;


public class FDSEE {

    public static PrintWriter writer;

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
   
    public static void listfiles(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);

        // get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                listfiles(file.toString(), files);
            }
        }
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
                    writer.println(file + "\t" + filetype);
                }

            } else if(isDirectory){
               
                ArrayList<File> files = new ArrayList<File>();
                
                listfiles(filename, files);
               
                for(File f: files){
                    
                    FileType filetype = getFileType(f.toString());
                    
                    if(filetype != null)
                        writer.println(f + "\t" + filetype);
                }
            }
        }

        writer.println("END-OF-LISTING");
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

            String action = args[0];
            String filename = args[1];

            String output;            
           
            switch(action){
                case "searchable":
                    
                    output = "FDSEE_searchable.txt";
                    writer = new PrintWriter(output, "UTF-8");
                    
                    isSearchable(filename);
                    writer.close();
                    
                    break;
                case "token":
                   
                    output = "FDSEE_searchable.txt";
                    writer = new PrintWriter(output, "UTF-8");
                    
                    isSearchable(filename);
                   
                    writer.close();
                    
                    new htmlParser("FDSEE.java");
                    
                    break;
                default:
                    break;
            }
    
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
