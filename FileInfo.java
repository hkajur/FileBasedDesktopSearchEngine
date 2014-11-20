/*
 * Harish Kajur
 * Jonathan Lysiak 4477
 *
 */

class FileInfo {
    
    private String filename;
    private FileType filetype;

    public FileInfo(){}

    public FileInfo(String filename,  FileType filetype){
        setFilename(filename);
        setFiletype(filetype);
    }

    public FileType getFileType(){
        return filetype;
    }
    
    public String getFilename(){
        return filename;
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
