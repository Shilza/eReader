package com.example.raw.app.Viewers;

import com.folioreader.model.HighLight;


import java.util.Date;

public class HighlightData implements HighLight {

    private String bookId;
    private String content;
    private Date date;
    private String type;
    private int pageNumber;
    private String pageId;
    private String rangy;
    private String uuid;
    private String note;

    public HighlightData(String bookId, String content, Date date,
                         String type, int pageNumber, String pageId,
                         String rangy, String uuid, String note){

        this.bookId = bookId;
        this.content = content;
        this.date = date;
        this.type = type;
        this.pageNumber = pageNumber;
        this.pageId = pageId;
        this.rangy = rangy;
        this.uuid = uuid;
        this.note = note;
    }

    @Override
    public String getBookId() {
        return bookId;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public int getPageNumber() {
        return pageNumber;
    }

    @Override
    public String getPageId() {
        return pageId;
    }

    @Override
    public String getRangy() {
        return rangy;
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public String getNote() {
        return note;
    }
}