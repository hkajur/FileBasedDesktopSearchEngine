/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 *
 */


import java.io.*;
import java.util.*;
import java.nio.file.*;

public class fdsee {

    public static final String SEARCH_OUTPUT = "fdsee_doclist.txt";

    public static PrintWriter writer;

    public static int docID = 1;

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
                filetype = FileType.NSTF;
                break;
        }

        return filetype;
    }
   
    public static void listFiles(String directoryName, ArrayList<File> files) {

        File directory = new File(directoryName);

        // Get all the files from a directory
        File[] fList = directory.listFiles();

        for (File file : fList){
            if (file.isFile()) 
                files.add(file);
            else if (file.isDirectory()) 
                listFiles(file.toString(), files);
        
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
                    if(filetype == FileType.NSTF)   
                        writer.println(docID + "\t" + filename + "\t" + "NSTF");
                    else
                        writer.println(docID + "\t" + filename + "\t" + "SETF" + "\t" + filetype);
                }

            } else if(isDirectory){
               
                ArrayList<File> files = new ArrayList<File>();
                
                listFiles(filename, files);
               
                for(File f: files){
                    
                    FileType filetype = getFileType(f.toString());
                    
                    if(filetype != null)
                        if(filetype == FileType.NSTF)   
                            writer.println(docID + "\t" + f + "\t" + "NSTF");
                        else
                            writer.println(docID + "\t" + f + "\t" + "SETF" + "\t" + filetype);

                    docID++;

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

            htmlParser parse;
            
            switch(action){
                
                case "searchable":
                    writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                    
                    isSearchable(filename);
                    writer.close();
                    
                    break;
               
                case "token":
                   
                    writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                    isSearchable(filename);
                    writer.close();
                    
                    parse = new htmlParser(ParseTokenType.TOKEN);
                    parse.closeWriter(); 
                    break;
                
                case "tokendebug":

                    writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                    isSearchable(filename);
                    writer.close();
                    
                    parse = new htmlParser(ParseTokenType.TOKENDEBUG);
                    parse.closeWriter(); 
                    break;
               
                case "stem":
                    break;
                default:
                    break;
            }
    
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
