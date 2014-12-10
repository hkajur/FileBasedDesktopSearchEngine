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
    public static int[] docIDList;

    public static Hashtable<String, Integer> hashWordList;

    public static boolean stringInArray(String word, String[] array){

        int length = array.length;

        for(int i = 0; i < length; i++)
            if(array[i].equals(word))
                return true;
    
        return false;
    }
    
    public static int[] mergeLists(int[] list1, int[] list2){
    
        int i = 0;
        int j = 0;

        int sizeTemp = list1.length + list2.length;

        int[] swit = list1;

        if(list1.length < list2.length){
            list1 = list2;
            list2 = swit;
        }
        int[] temp = new int[sizeTemp];

        for(int index = 0; index < sizeTemp; index++){
            
            if(i < list1.length && j < list2.length){
            
                if(list1[i] < list2[j])
                    temp[index] = list1[i++];
                else if(list1[i] > list2[j])
                    temp[index] = list2[j++];
                else 
                    temp[index] = list1[i++];

            } else {
               if(i < list1.length){
                   temp[index] = list1[i++]; 
               } else if(j < list1.length){
                   temp[index] = list2[j++];
               }
            }

        }

        if(i != list1.length)
            temp[sizeTemp - 1] = list1[i];
        else if(j != list2.length)
            temp[sizeTemp - 1] = list2[j];

        return temp;
    }
   
    // searching AND statemnt
    
    public static int[] findDuplicates(int[] list){
       
        if(list == null)
            return null;

        int size = list.length - 1;

        ArrayList<Integer> array = new ArrayList<Integer>();
       
        for(int i = 0; i < size; i++){
            if(list[i] == list[i+1]){
                array.add(list[i]);
            }
        }

        Integer[] arr = new Integer[array.size()];
        arr = array.toArray(arr);
      
        int[] array2 = new int[array.size()];

        for(int i = 0; i < array.size(); i++){
            array2[i] = arr[i];
        }

        return array2; 
    }

    /*
     * Turns 111333455
     * into 1345
     */

    public static int[] reducer(int[] list){

        int size = list.length ;
       
        if(list == null)    
            return null;
        ArrayList<Integer> array = new ArrayList<Integer>();
      
        if(size == 1){
            int[] c = new int[1];
            c[0] = list[0];
            return c;
        }

        if(list[0] != list[1]){
            array.add(list[0]);
        }

        for(int i = 1; i < size; i++){
            if(list[i] != list[i-1]){
                array.add(list[i]);
            }
        }

        Integer[] arr = new Integer[array.size()];
        arr = array.toArray(arr);
      
        int[] array2 = new int[array.size()];

        for(int i = 0; i < array.size(); i++){
            array2[i] = arr[i];
        }

        return array2; 
    }

    public static int[] getDocsForTerm(String term){
        
        File f = new File("fdsee_invindex/" + term);

        if(!f.exists())
            return null;
        
        int[] array = null;

        try {

        BufferedReader br = new BufferedReader(new FileReader(f));

        String line = null;

        int prevDocID = -1;
       
        ArrayList<Integer> al = new ArrayList<Integer>();

        while( (line = br.readLine()) != null){
            
            String[] arr = line.split(",");

            int docID = Integer.parseInt(arr[0]);

            if(docID != prevDocID)
                al.add(docID);

            prevDocID = docID;
        }
        
        array = new int[al.size()];

        for(int i = 0; i < al.size(); i++){
            array[i] = al.get(i);
        }


        } catch(Exception ex){
            ex.printStackTrace();
        }
        
        return array;
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
  
    public static Hashtable<Integer, String> mergeTokDebug() throws Exception{
    
        File tk = new File("fdsee_token.txt");
        File tkDbg = new File("fdsee_tokendebug.txt");
        
        BufferedReader brTk = new BufferedReader(new FileReader(tk));
        BufferedReader brTkDbg = new BufferedReader(new FileReader(tkDbg));

        String line = null;
        String line2 = null;

        Hashtable<Integer, String> hs = new Hashtable<Integer, String>();

        int pID = -1;
       
        int numOfLines = 0;
        
        while( ((line = brTk.readLine()) != null) && ((line2 = brTkDbg.readLine()) != null)){
          
            line = line.substring(1, line.length()-1);
            line2 = line2.substring(1, line2.length()-1);

            String[] array1 = line.split(",");
            String[] array2 = line2.split(",");

            hs.put(Integer.parseInt(array1[0]), array2[0]);

            if(pID != Integer.parseInt(array1[0])){
                numOfLines++;
            }

            pID = Integer.parseInt(array1[0]);
        }

        docIDList = new int[numOfLines];
        
        brTk.close();
        brTkDbg.close();

        brTk = new BufferedReader(new FileReader(tk));

        int i = 0;
       
        pID = -1;

        while( (line = brTk.readLine()) != null){
            
            line = line.substring(1, line.length() - 1);
            String[] array1 = line.split(",");

            if(pID != Integer.parseInt(array1[0]))
                docIDList[i++] = Integer.parseInt(array1[0]);

            pID = Integer.parseInt(array1[0]);
        }


        return hs;
    }

    public static String getDocByDocID(Hashtable<Integer, String> hs, int docID){
        return hs.get(docID);
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

            hashWordList.put(word, wordID);
        
        }

        br.close();

        br = new BufferedReader(new FileReader(file1));

        while( (line = br.readLine()) != null){
            
            String[] array1 = line.split("\t");
                
            int wordID = Integer.parseInt(array1[0]);
            String word = array1[1];
            String st = array1[2];
            String stemmedWord = stemming.stem(word);
            
            if(hashWordList.containsKey(stemmedWord)){
                pw.println(wordID + "\t" + word + "\t" + st + "\t" + hashWordList.get(stemmedWord));
            } else {
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
            
            if(args.length > 4){
                System.err.println("Error: Can't have more than four argument");
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

                case "invert":
                    invert.vocab(LEXICON_OUTPUT);
                    break;

                case "and2":

                    if(args.length != 3){
                        System.out.println("Error: Program requires three arguments");
                        System.exit(0);
                    } else {
                        
                        String term1 = (args[1]);
                        String term2 = (args[2]);
                
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));
                        
                        int[] mergedList = mergeLists(term1Docs, term2Docs);
             
                        int[] dupOnly = findDuplicates(mergedList);

                        Hashtable<Integer, String> hs = mergeTokDebug();
               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }

                    break;
                
                case "and3":
                   
                    if(args.length != 4){
                        System.out.println("Error: Program requires four arguments");
                        System.exit(0);
                    } else {

                        String term1 = (args[1]);
                        String term2 = (args[2]);
                        String term3 = (args[3]);
                
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));

                        int[] term3Docs = getDocsForTerm(term3);

                        if(term3Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term3));
                        
                        int[] mergedList = mergeLists(term1Docs, term2Docs);
                        mergedList = mergeLists(mergedList, term3Docs);

                        int[] dupOnly = findDuplicates(mergedList);

                        Hashtable<Integer, String> hs = mergeTokDebug();
               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }

                    break;

                case "or2":
                    
                    if(args.length != 3){
                        System.out.println("Error: Program requires three arguments");
                        System.exit(0);
                    } else {
                        
                        String term1 = (args[1]);
                        String term2 = (args[2]);
                
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));
                        
                        int[] mergedList = mergeLists(term1Docs, term2Docs);
             
                        int[] dupOnly = reducer(mergedList);

                        Hashtable<Integer, String> hs = mergeTokDebug();
               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }
    
                    break;
                case "or3":
    
                    if(args.length != 4){
                        System.out.println("Error: Program requires four arguments");
                        System.exit(0);
                    } else {

                        String term1 = (args[1]);
                        String term2 = (args[2]);
                        String term3 = (args[3]);
                
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));

                        int[] term3Docs = getDocsForTerm(term3);

                        if(term3Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term3));
                        
                        int[] mergedList = mergeLists(term1Docs, term2Docs);
                        mergedList = mergeLists(mergedList, term3Docs);

                        int[] dupOnly = reducer(mergedList);

                        Hashtable<Integer, String> hs = mergeTokDebug();
               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }
                    break;

                case "andnot":
                    
                    if(args.length != 3){
                        System.out.println("Error: Program requires three arguments");
                        System.exit(0);
                    } else {
                        
                        String term1 = (args[1]);
                        String term2 = (args[2]);
                
                        Hashtable<Integer, String> hs = mergeTokDebug();
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));
                       
                        term2Docs = mergeLists(term2Docs, docIDList);
                        
                        int[] mergedList = removeDup(term2Docs);
                        mergedList = mergeLists(term1Docs, mergedList);
             
                        int[] dupOnly = findDuplicates(mergedList);

               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }
                
                    break;

                case "and2not":
                    
                    if(args.length != 4){
                        System.out.println("Error: Program requires four arguments");
                        System.exit(0);
                    } else {
                        
                        String term1 = (args[1]);
                        String term2 = (args[2]);
                        String term3 = (args[3]);
                
                        Hashtable<Integer, String> hs = mergeTokDebug();
                        int[] term1Docs = getDocsForTerm(term1);

                        if(term1Docs == null)
                            term1Docs = getDocsForTerm(stemming.stem(term1));
                        
                        int[] term2Docs = getDocsForTerm(term2);

                        if(term2Docs == null)
                            term2Docs = getDocsForTerm(stemming.stem(term2));
                        
                        int[] term3Docs = getDocsForTerm(term3);

                        if(term3Docs == null)
                            term3Docs = getDocsForTerm(stemming.stem(term3));
                       
                        term3Docs = mergeLists(term3Docs, docIDList);
                        
                        int[] mergedList = removeDup(term3Docs);
                        int[] mergeTerm12 = findDuplicates(mergeLists(term1Docs, term2Docs));
                        mergedList = mergeLists(mergeTerm12, mergedList);
             
                        int[] dupOnly = findDuplicates(mergedList);

               
                        if(dupOnly != null)
                            for(int i = 0; i < dupOnly.length; i++)
                                System.out.println(hs.get(dupOnly[i]));
                    }
                    break;

                case "next5":

                    break;
                default:
                    break;
            }
    
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public static void printIntArray(int[] array){
        
        if(array == null)
            return;

        for(int i = 0; i < array.length; i++)
            System.out.println(array[i]);
    }
   
    /*
     * 1113345
     * 45
     */

    public static int[] removeDup(int[] list){
       
        if(list == null)
            return null;

        int mark = -1;
        
        ArrayList<Integer> arrList = new ArrayList<Integer>();

        for(int i = 0; i < list.length - 1; i++){
            if(list[i] != list[i+1]){
                if(list[i] != mark)
                    arrList.add(list[i]);
            } else{
                mark = list[i];
            }
        }

        if(mark != list[list.length - 1])
            arrList.add(list[list.length - 1]);

        int[] array = new int[arrList.size()];

        for(int i = 0; i < array.length; i++)
            array[i] = arrList.get(i);

        arrList = null;

        return array;
    }

}
