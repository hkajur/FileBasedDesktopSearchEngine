/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 *
 */


import java.io.*;
import java.util.*;

class htmlParser {
 
    private File file;
    private BufferedReader br;
    private PrintWriter writer;

    private List<FileInfo> fileinfoList;

    private int attr;

    private ParseTokenType ttype;
    
    public List<String> wordList;

    public int docID = 1;
    public int wordID = 1;

    public htmlParser(ParseTokenType ttype){
    
        try {
      
            this.ttype = ttype;

            fileinfoList = new ArrayList<FileInfo>();
            
            if(ttype == ParseTokenType.TOKEN)
                writer = new PrintWriter("fdsee_lexicon.txt", "UTF8");
            else 
                writer = new PrintWriter("fdsee_tokendebug.txt", "UTF8");

            wordList = new ArrayList<String>();

            getFileInformation();
            
            for(FileInfo fInfo : fileinfoList){
               
                String fname = fInfo.getFilename();
                FileType filetype = fInfo.getFileType();

                switch(filetype){
                    case HTML:
                        attr = 0;
                        parseHtmlFile(fname);
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

    public String getWordByWordID(int index){
        
        String str = (String) (wordList.get(index - 1));

        return str;
    }

    public int getValueOfTokenStatus(TokenStatus ts){
       
        int n = 0;
        
        switch(ts){
            case REGULAR:
                n = 0;
                break;
            case INSIDE_TITLE:
                n = 1;
                break;
            case INSIDE_A:
                n = 2;
                break;
            case INSIDE_H1_3:
                n = 3;
                break;
            case INSIDE_H4_6:
                n = 4;
                break;
            default:
                break;

        }

        return n;
    }

    public String getTokenStatus(TokenStatus ts){
        
        String str = "";
        int n  = 0;

        switch(ts){
            case REGULAR:
                str = "0";
                break;
            case INSIDE_TITLE:
                str = "<TITLE>";
                break;
            case INSIDE_A:
                str = "<A>";
                break;
            case INSIDE_H1_3:
                str = "<H1>";
                break;
            case INSIDE_H4_6:
                str = "<H4>";
                break;
            default:
                break;

        }

        return str;
    }

    public void print(String fname, StringBuffer str, TokenStatus ts, int n){
        switch(ttype){
            case TOKEN:
                writer.println("(" + docID + "," 
                        + wordID + "," + n + "," 
                        + getValueOfTokenStatus(ts) + ")");
                break;
            case TOKENDEBUG:
                writer.println("(" + fname + "," 
                        + str.toString().toLowerCase() + "," 
                        + n + "," + getTokenStatus(ts) + ")");
                break;
            default:
                break;
        }

    }
    public void parseHtmlFile(String fname){

        try {
            
            File file = new File(fname);
            br = new BufferedReader(new FileReader(file));

            String line = null;
        
            int wpos = 1;

            int chVal;

            TokenType tt = TokenType.START_STATE;
            TokenStatus ts = TokenStatus.REGULAR;

            StringBuffer sbTagName = new StringBuffer();
            StringBuffer sbWord = new StringBuffer();

            char ch;

            int wordOffset = 1;

            while((chVal = br.read()) != -1){

                ch = (char)chVal;
                
                switch(tt){
                    case START_STATE:
                        if(ch == '<'){
                            tt = TokenType.OPEN_GREAT;
                        } else if(Character.isLetterOrDigit(ch)){
                            sbWord.append(ch);
                            tt = TokenType.WORD;
                        } else if(Character.isWhitespace(ch)){
                            tt = TokenType.START_STATE;
                        } else {
                            tt = TokenType.USELESS_CHAR;
                        }
                        break;
                    case OPEN_GREAT:
                        if(Character.isWhitespace(ch)){
                            tt = TokenType.OPEN_GREAT;
                        } else if(Character.isLetter(ch)){
                            tt = TokenType.TAG_NAME;
                            sbTagName.append(ch);
                        } else if(ch == '/'){

                            tt = TokenType.SLASH;
                        } else if(ch == '!'){
                            tt = TokenType.BANG;
                        }
                        break;
                    case BANG:
                        if(ch == '>')
                            tt = TokenType.CLOSE_GREAT;
                        break;
                    case WORD:
                        if(Character.isLetterOrDigit(ch)){
                            sbWord.append(ch);
                        } else if(Character.isWhitespace(ch)){
                            print(fname, sbWord, ts, wordOffset);
                            wordOffset++;
                            sbWord = new StringBuffer();
                            tt = TokenType.WORD_SPACE;
                        } else if(ch == '<'){
                            print(fname, sbWord, ts, wordOffset);
                            wordOffset++;
                            sbWord = new StringBuffer();
                            tt = TokenType.OPEN_GREAT;
                        }
                        else {
                            print(fname, sbWord, ts, wordOffset);
                            wordOffset++;
                            sbWord = new StringBuffer();
                            tt = TokenType.USELESS_CHAR;
                        }
                        break;

                    case WORD_SPACE:
                        if(Character.isWhitespace(ch)){
                            tt = TokenType.WORD_SPACE;
                        } else if(Character.isLetterOrDigit(ch)){
                            sbWord.append(ch);
                            tt = TokenType.WORD;
                        } else if(ch == '<'){
                            tt = TokenType.OPEN_GREAT;
                        } else {
                            tt = TokenType.USELESS_CHAR;
                        }
                        break;

                    case USELESS_CHAR:
                        if(Character.isLetterOrDigit(ch)){
                            sbWord.append(ch);
                            tt = TokenType.WORD;
                        } else if(ch == '<'){
                            tt = TokenType.OPEN_GREAT;
                        }
                        break;
                    case SLASH:
                        if(Character.isLetter(ch)){
                            sbTagName.append(ch);
                            tt = TokenType.SLASH_TAG_NAME;
                        } else 
                            tt = TokenType.BAD_TAG_NAME;
                            break;
                    case SLASH_TAG_NAME:
                        if(Character.isLetterOrDigit(ch)){
                            sbTagName.append(ch);
                            tt = TokenType.SLASH_TAG_NAME;

                        } else if(ch == '>'){

                            String str = sbTagName.toString().toLowerCase();
                            
                            if(str.matches("a"))
                                ts = TokenStatus.REGULAR;
                            else if(str.equals("title"))
                                ts = TokenStatus.REGULAR;
                            else if(str.matches("h[1-3]"))
                                ts = TokenStatus.REGULAR;
                            else if(str.matches("h[4-6]"))
                                ts = TokenStatus.REGULAR;

                            sbTagName = new StringBuffer();
                            
                            tt = TokenType.CLOSE_GREAT;
                        } else 
                            tt = TokenType.BAD_TAG_NAME;
                        break;
                    case TAG_NAME:

                        if(Character.isLetterOrDigit(ch)){

                            sbTagName.append(ch);
                            tt = TokenType.TAG_NAME;

                        } else if(Character.isWhitespace(ch)){
                           
                            String str = sbTagName.toString().toLowerCase();
                            
                            if(str.matches("a"))
                                ts = TokenStatus.INSIDE_A;
                            else if(str.equals("title"))
                                ts = TokenStatus.INSIDE_TITLE;
                            else if(str.matches("h[1-3]"))
                                ts = TokenStatus.INSIDE_H1_3;
                            else if(str.matches("h[4-6]"))
                                ts = TokenStatus.INSIDE_H4_6;

                            sbTagName = new StringBuffer();

                            tt = TokenType.OPEN_GREAT_SPACE;

                        } else if(ch == '>'){

                            String str = sbTagName.toString().toLowerCase();
                            
                            if(str.matches("a"))
                                ts = TokenStatus.INSIDE_A;
                            else if(str.equals("title"))
                                ts = TokenStatus.INSIDE_TITLE;
                            else if(str.matches("h[1-3]"))
                                ts = TokenStatus.INSIDE_H1_3;
                            else if(str.matches("h[4-6]"))
                                ts = TokenStatus.INSIDE_H4_6;

                            sbTagName = new StringBuffer();
                            
                            tt = TokenType.CLOSE_GREAT;
                        } else 
                            tt = TokenType.BAD_TAG_NAME;
                        break;
                    case BAD_TAG_NAME:
                        if(ch == '>')
                            tt = TokenType.CLOSE_GREAT;
                        break;

                    case OPEN_GREAT_SPACE:
                        if(ch == '>')
                            tt = TokenType.CLOSE_GREAT;
                        break;

                    case CLOSE_GREAT:
                        if(Character.isLetterOrDigit(ch)){
                            sbWord.append(ch);
                            tt = TokenType.WORD;
                        }else if(Character.isWhitespace(ch)){
                            tt = TokenType.CLOSE_GREAT;
                        } else if(ch == '<'){
                            tt = TokenType.OPEN_GREAT;
                        }else
                            tt = TokenType.USELESS_CHAR;
                        break;

                }
            }


        } catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void parseTextFile(String fname){

        try {
            
            File file = new File(fname);
            br = new BufferedReader(new FileReader(file));

            String line = null;
        
            int wpos = 1;

            while((line = br.readLine()) != null){

                line = line.replaceAll("[^A-Za-z0-9]", " ").toLowerCase();
                line = line.replaceAll("[ ][ ]*", " ");
                line = line.replaceAll("^[ ]*", "");

                String[] strArray = line.split(" ");

                for(String str : strArray){
                    
                    if(!str.isEmpty() && str.trim().length() > 0){
                       
                        wordList.add(str);

                        switch(ttype){

                            case TOKEN:
                                writer.println("(" + docID + "," 
                                + wordID + "," + wpos 
                                + "," + attr + ")");

                                break;
                            case TOKENDEBUG:
                                writer.println("(" + fname + "," 
                                + str + "," + wpos 
                                + "," + attr + ")");

                                break;
                            default:
                                break;
                        }
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
      
        String inputFile = "FDSEE_doclist.txt";

        FileReader fr = new FileReader(inputFile);
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

    public void closeWriter(){
        writer.close();
    }
}
