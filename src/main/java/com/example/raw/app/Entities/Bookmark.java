package com.example.raw.app.Entities;

public class Bookmark {

    private int page;
    private long uploadDate;
    private String text;

    public Bookmark(int page, long uploadDate, String text){
        this.page = page;
        this.uploadDate = uploadDate;
        this.text = text;
    }

    public int getPage() {
        return page;
    }

    public long getUploadDate(){
        return uploadDate;
    }

    public String getText() {
        return text;
    }

}
