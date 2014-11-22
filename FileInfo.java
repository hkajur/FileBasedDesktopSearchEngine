/*
 * Venkata Harish Kajur 8982
 * Jonathan Lysiak 4477
 * CS-345
 * Homework 6
 */

/*
 * FileInfo is intended to store the information of each individual file.
 */
class FileInfo {

    private int docID;
    private String filename;
    private FileType filetype;

    public FileInfo(){}

    /*
     * Passes the filename, filetype, and docID variables then calls the setter methods to store
     */
    public FileInfo(String filename,  FileType filetype, int docID){
        setFilename(filename);
        setFiletype(filetype);
        setDocID(docID);
    }

    /*
     *  Retrieves the current filetype
     */
    public FileType getFileType(){
        return filetype;
    }

    /*
     *  Retrieves the current filename
     */    
    public String getFilename(){
        return filename;
    }

    /*
     *  Retrieves current docID
     */
    public int getDocID(){
        return docID;
    }

    /*
     *  Sets docID
     */   
    private void setDocID(int docID){
        this.docID = docID;
    }

    /*
     *  Sets filename
     */  
    private void setFilename(String filename){
        this.filename = filename;
    }

    /*
     *  Sets filetype
     */  
    private void setFiletype(FileType filetype){
        this.filetype = filetype;
    }

    /*
     *  Returns the filename and filetype as a string
     */    
    public String toString(){
        return "FileName: " + filename + "\tFileType: " + filetype.toString();
    }
}
