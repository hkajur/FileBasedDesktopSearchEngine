import java.util.*;
import java.io.*;

public class invert {

    public static void vocab(String filename){

        try {


            Hashtable<String, Integer> hashtable = new Hashtable<String, Integer>();
            
            BufferedReader br = new BufferedReader(new FileReader(filename));

            String line = null;

            while( (line = br.readLine()) != null){

                String[] array = line.split("\t");
                int wordID = Integer.parseInt(array[0]);
                int stemmedWordID = Integer.parseInt(array[3]);
                String word = array[1];

                if(wordID == stemmedWordID){    
                    hashtable.put(word, stemmedWordID);
                }
            }

            br.close();

            File dirName = new File("fdsee_invindex");
            if(!dirName.exists()){
                dirName.mkdir();
            }

            List<String> keys = new ArrayList<String>(hashtable.keySet());
                
            Collections.sort(keys);

            PrintWriter voc = new PrintWriter("fdsee_vocabulary.txt", "UTF-8");
            
            for(String key : keys){
                
                int value = hashtable.get(key);

                String f = "fdsee_invindex/" + key ;

                PrintWriter pw = new PrintWriter(f, "UTF-8");
                br = new BufferedReader(new FileReader("fdsee_token.txt"));
               
                int nDocs = 0;
                int nHits = 0;

                int pDocID = -1;
               
                while( (line = br.readLine()) != null){
                    
                    line = line.substring(1, line.length() -1);
                    
                    String[] lineArray = line.split(",");

                    int wID = Integer.parseInt(lineArray[1]);
                    
                    if(value == wID){
                        
                        int docID = Integer.parseInt(lineArray[0]);
                        int wOffset = Integer.parseInt(lineArray[2]);
                        int attr = Integer.parseInt(lineArray[3]);

                        if(docID != pDocID)
                            nDocs++;

                        pDocID = docID;
                        nHits++;

                        pw.println(docID + "," + wOffset + "," + attr);
                    }
                
                }

                voc.println(key + "," + value + "," + nDocs + "," + nHits);

                br.close();
                pw.close();
                
            }

            voc.close();

            } catch(Exception ex){
                ex.printStackTrace();
            }
    }

}
