/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 * CS-345
 * Homework 6
 */


import java.io.*;
import java.util.*;
import java.nio.file.*;

public class fdsee {

    public static final String SEARCH_OUTPUT = "fdsee_doclist.txt";
    public static final String TOKEN_OUTPUT = "fdsee_token.txt";
    public static final String TOKENDEBUG_OUTPUT = "fdsee_tokendebug.txt";
    public static final String STOPWORD_OUTPUT = "fdsee_stopword.txt";
    public static final String LEXICON_OUTPUT = "fdsee_lexicon.txt";

    public static final String[] stopWords = {"i", "a", "about", "an", "are", "as", "at", "be", "by", "com", "en", "for", "from", "how", "in", "is", "it", "of", "on", "or", "that", "this", "to", "was", "what", "when", "where", "who", "will", "with", "www"};

    public static PrintWriter writer;

    public static int docID = 1;

    public static Hashtable<String, Integer> hashWordList;

    public static boolean stringInArray(String word, String[] array){

        int length = array.length;

        for(int i = 0; i < length; i++)
            if(array[i].equals(word))
                return true;
    
        return false;
    }

    public static void mergeAndStopWords(File file1, File file2) throws Exception {

        BufferedReader br1 = new BufferedReader(new FileReader(file1));
        BufferedReader br2 = new BufferedReader(new FileReader(file2));

        PrintWriter pw = new PrintWriter(STOPWORD_OUTPUT, "UTF-8");

        String line1;
        String line2;

        while(((line1 = br1.readLine()) != null)
           && ((line2 = br2.readLine()) != null)){

            line1 = line1.substring(1, line1.length() - 1);
            line2 = line2.substring(1, line2.length() - 1);

            String[] array1 = line1.split(",");
            String[] array2 = line2.split(",");

            int wordID = Integer.parseInt(array1[1]);
            String word = array2[1];

            if(stringInArray(word, stopWords)){
                pw.println(wordID + "\t" + word + "\t" + "STOPWORD");
            } else{
                pw.println(wordID + "\t" + word + "\t" + "NONSTOPWORD");
            }

        }
    
        br1.close();
        br2.close();

        pw.close();
    }
   
    public static void mergeStem(File file1) throws Exception {
    
        BufferedReader br = new BufferedReader(new FileReader(file1));

        String line = null;
        PrintWriter pw = new PrintWriter(LEXICON_OUTPUT, "UTF-8");

        hashWordList = new Hashtable<String, Integer>();

        while( ((line = br.readLine()) != null)){

            String[] array1 = line.split("\t");
                
            int wordID = Integer.parseInt(array1[0]);
            String word = array1[1];
            String st = array1[2];
            String stemmedWord = stemming.stem(word);

            if(hashWordList.containsKey(stemmedWord)){
                pw.println(wordID + "\t" + word + "\t" + st + "\t" + hashWordList.get(stemmedWord));
            } else {
                hashWordList.put(word, wordID);
                pw.println(wordID + "\t" + word + "\t" + st + "\t" + wordID);
            }
        
        }

        pw.close();

        br.close();
    }

    /*
     * Pull files and reads their extensions and sets a filetype
     */
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
   
    /*
     * Takes a directory and adds content files into an arraylist
     */
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
    
    /*
     * Checks files to determine if files are searchable 
     */
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

    /*
     * Check if files exist
     */
    public static boolean fileExists(File f){
        if(f.exists())
            return true;
        return false;
    }

    /*
     * Main Method
     */
    public static void main(String[] args){
        
        try {

            //Check that the arguments are valid

            if(args.length == 0){
                System.err.println("Error: Need at least one argument");
                System.exit(1);
            }
            
            if(args.length > 2){
                System.err.println("Error: Can't have more than two argument");
                System.exit(1);
            }

            String action = args[0];
            String filename = null;

            if(args.length == 2)
                if(args[1] != null)
                    filename = args[1];

            htmlParser parse;
            
            switch(action){
                
                case "searchable":
                    
                    writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                    isSearchable(filename);
                    writer.close();
                    
                    break;
               
                case "token":
                  
                    if(filename != null){
                        writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                        isSearchable(filename);
                        writer.close();
                    } 

                    parse = new htmlParser(ParseTokenType.TOKEN);
                    parse.closeWriter(); 
                    break;
                
                case "tokendebug":

                    if(filename != null){
                        writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                        isSearchable(filename);
                        writer.close();
                    }

                    parse = new htmlParser(ParseTokenType.TOKENDEBUG);
                    parse.closeWriter(); 
                    break;
              
                case "stopword":
                    
                    if(filename != null){
                  
                        writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                        isSearchable(filename);
                        writer.close();
                    
                        parse = new htmlParser(ParseTokenType.TOKEN);
                        parse.closeWriter(); 
                    
                        parse = new htmlParser(ParseTokenType.TOKENDEBUG);
                        parse.closeWriter(); 

                    }
                    
                    mergeAndStopWords(new File(TOKEN_OUTPUT), new File(TOKENDEBUG_OUTPUT));
                    break; 
                case "stem":
                    
                    if(filename != null){
                  
                        writer = new PrintWriter(SEARCH_OUTPUT, "UTF-8");
                        isSearchable(filename);
                        writer.close();
                    
                        parse = new htmlParser(ParseTokenType.TOKEN);
                        parse.closeWriter(); 
                    
                        parse = new htmlParser(ParseTokenType.TOKENDEBUG);
                        parse.closeWriter(); 

                        mergeAndStopWords(new File(TOKEN_OUTPUT), new File(TOKENDEBUG_OUTPUT));
                    }
                    
                    mergeStem(new File(STOPWORD_OUTPUT));
                    break;

                default:
                    break;
            }
    
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
