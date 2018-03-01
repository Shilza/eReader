package com.example.raw.app;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Book implements Serializable{

    private String name;
    private String filePath;
    private String size;
    private String totalRead;
    private String lastActivity;
    private int coverId;
    private boolean isFix;

    Book(String name, String filePath, float size, int coverId){
        this.name = name;
        this.filePath = filePath;

        DecimalFormat f = new DecimalFormat("##.00");
        String strSize = f.format(size);
        if(size < 1)
           strSize = "0" + strSize;
        this.size = String.valueOf(strSize) + " Мб";

        this.coverId = coverId;
        this.totalRead = "0%";
        this.lastActivity = "0:00:00";
        this.isFix = false;
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

    String getLastActivity(){
        return lastActivity;
    }

    int getCoverId(){
        return coverId;
    }

    boolean isFix() { return isFix; }
}
