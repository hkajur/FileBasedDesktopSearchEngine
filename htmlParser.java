import java.io.*;
import java.util.*;

class htmlParser {
 
    private File file;
    private BufferedReader br;
    private List<FileInfo> fileinfoList;

    private int attr;

    public int docID = 1;
    public int wordID = 1;

    public int wpos = 1;
    public htmlParser(){}

    public htmlParser(String filename){
    
        try {
       
            fileinfoList = new ArrayList<FileInfo>();
            
            getFileInformation();
            
            for(FileInfo fInfo : fileinfoList){
               
                String fname = fInfo.getFilename();
                FileType filetype = fInfo.getFileType();

                switch(filetype){
                    case HTML:
                        break;
                    case TEXT:
                        attr = 0;
                        parseTextFile(fname);
                        break;
                    default:
                        break;
                }

                docID++;
            }
            
        } catch(FileNotFoundException fex){
          
            System.err.println("Error: " + fex);
        
        } catch(Exception ex){
            
            System.err.println("Error: " + ex);
        
        }
    }

    public void parseTextFile(String fname){

        try {
            
            File file = new File(fname);
            br = new BufferedReader(new FileReader(file));

            String line = null;
        
            wpos = 1;

            while((line = br.readLine()) != null){

                line = line.replaceAll("[^A-Za-z0-9]", " ").toLowerCase();
                line = line.replaceAll("[ ][ ]*", " ");
                line = line.replaceAll("^[ ]*", "");

                String[] strArray = line.split(" ");

                for(String str : strArray){
                    if(!str.isEmpty() && str.trim().length() > 0){
                        //System.out.println(str);
                        System.out.println("(" + docID + "," + wordID + "," + wpos + "," + attr + ")");
                        wordID++;
                        wpos++;
                    }
                }

            }
        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public FileType getFileType(String str){
        
        FileType filetype = null;
        
        switch(str){
            case "TEXT":
                filetype = FileType.TEXT;
                break;
            case "HTML":
                filetype = FileType.HTML;
                break;
            default:
                break;
        }

        return filetype;
    }

    public void getFileInformation() throws IOException{
       
        FileReader fr = new FileReader("FDSEE_searchable.txt");
        BufferedReader bRead = new BufferedReader(fr);

        String line = null;
        String[] array = null;
        
        while((line = bRead.readLine()) != null){
            
            array = line.split("\t");
        
            if(array.length == 2){

               String filename = array[0];
               String filetypeStr = array[1];

               FileType filetype = getFileType(filetypeStr);
               fileinfoList.add(new FileInfo(filename, filetype));
            
            }

        }
    }

    public void print(){
        
        for(FileInfo f: fileinfoList){
            System.out.println(f);
        }

    }

    public String parse(){

        String line = null;
       
        try {
        
            while((line = br.readLine()) != null){
                
                line = line.toLowerCase();
            }

        } catch(IOException ex){
            System.err.println("Error: " + ex);
        }

        return line;
    }
}
