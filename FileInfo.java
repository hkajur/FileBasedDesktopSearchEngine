/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 * CS-345
 * Homework 6
 */

class FileInfo {

    private int docID;
    private String filename;
    private FileType filetype;

    public FileInfo(){}

    public FileInfo(String filename,  FileType filetype, int docID){
        setFilename(filename);
        setFiletype(filetype);
        setDocID(docID);
    }

    public FileType getFileType(){
        return filetype;
    }
    
    public String getFilename(){
        return filename;
    }

    public int getDocID(){
        return docID;
    }
   
    private void setDocID(int docID){
        this.docID = docID;
    }

    private void setFilename(String filename){
        this.filename = filename;
    }

    private void setFiletype(FileType filetype){
        this.filetype = filetype;
    }

    public String toString(){
        return "FileName: " + filename + "\tFileType: " + filetype.toString();
    }
}
