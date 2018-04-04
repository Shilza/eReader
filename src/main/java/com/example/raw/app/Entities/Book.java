package com.example.raw.app.Entities;


import com.example.raw.app.Extensions;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Book implements Serializable{

    private String name;
    private String filePath;
    private String size;
    private float totalRead;
    private long lastActivity;
    private long timeOfReading;
    private Extensions extension;
    private ArrayList<Bookmark> bookmarks;

    public Book(String name, String filePath, String size, long lastActivity, Extensions extension){
        this.name = name;
        this.filePath = filePath;
        this.size = size;
        this.lastActivity = lastActivity;
        this.totalRead = 0;
        this.timeOfReading = 0;
        this.extension = extension;
        this.bookmarks = new ArrayList<>();
    }

    public ArrayList<Bookmark> getBookmarks() {
        return bookmarks;
    }

    public void addBookmark(Bookmark bookmark){
        bookmarks.add(bookmark);
    }

    public void setLastActivity(long lastActivity){
        this.lastActivity = lastActivity;
    }

    public void setTotalRead(float totalRead){
        this.totalRead = totalRead;
    }

    public String getName(){
        return name;
    }

    public String getFilePath(){
        return filePath;
    }

    public String getSize(){
        return size;
    }

    public float getTotalRead(){
        return totalRead;
    }

    public String getLastActivity(){
        return lastActivityProcessing(lastActivity);
    }

    public long getTimeOfReading() {
        return timeOfReading;
    }

    public void setTimeOfReading(long timeOfReading) {
        this.timeOfReading = timeOfReading;
    }

    public Extensions getExtension() { return extension; }

    private String lastActivityProcessing(long lastActivity){
        SimpleDateFormat sdf;

        if(lastActivity+86400000 > (System.currentTimeMillis())) //60*60*24*1000 one day
            sdf = new SimpleDateFormat("h:mm a", Locale.getDefault());
        else
            sdf = new SimpleDateFormat("d MMMM yyyy", Locale.getDefault());

        return sdf.format(lastActivity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Book book = (Book) o;

        if (name != null ? !name.equals(book.name) : book.name != null) return false;
        if (filePath != null ? !filePath.equals(book.filePath) : book.filePath != null)
            return false;
        if (size != null ? !size.equals(book.size) : book.size != null) return false;
        return extension == book.extension;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        result = 31 * result + (size != null ? size.hashCode() : 0);
        result = 31 * result + (extension != null ? extension.hashCode() : 0);
        return result;
    }
}
