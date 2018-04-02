package com.example.raw.app.Entities;

public class Bookmark{

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Bookmark bookmark = (Bookmark) o;

        if (page != bookmark.page) return false;
        if (uploadDate != bookmark.uploadDate) return false;
        return text != null ? text.equals(bookmark.text) : bookmark.text == null;
    }

    @Override
    public int hashCode() {
        int result = page;
        result = 31 * result + (int) (uploadDate ^ (uploadDate >>> 32));
        result = 31 * result + (text != null ? text.hashCode() : 0);
        return result;
    }
}
