package com.example.raw.app;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Book implements Serializable{

    private String name;
    private String filePath;
    private String size;
    private String totalRead;
    private long lastActivity;
    private int coverId;

    Book(String name, String filePath, String size, int coverId){
        this.name = name;
        this.filePath = filePath;
        this.size = size;
        this.coverId = coverId;
        this.totalRead = "0%";
        this.lastActivity = 0;
    }

    void setLastActivity(long lastActivity){
        this.lastActivity = lastActivity;
    }

    String getName(){
        return name;
    }

    String getFilePath(){
        return filePath;
    }

    String getSize(){
        return size;
    }

    String getTotalRead(){
        return totalRead;
    }

    long getLastActivity(){
        return lastActivity;
    }

    int getCoverId(){
        return coverId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (coverId != book.coverId) return false;
        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (filePath != null ? !filePath.equals(book.filePath) : book.filePath != null)
            return false;
        if (size != null ? !size.equals(book.size) : book.size != null) return false;
        return totalRead != null ? totalRead.equals(book.totalRead) : book.totalRead == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (totalRead != null ? totalRead.hashCode() : 0);
        result = 31 * result + (int) (lastActivity ^ (lastActivity >>> 32));
        result = 31 * result + coverId;
        return result;
    }
}
