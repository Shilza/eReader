package com.example.raw.app;

import java.io.Serializable;
import java.text.DecimalFormat;

public class Book implements Serializable{
    String name;
    String filePath;
    String size;
    int coverId;

    Book(String name, String filePath, float size, int coverId){
        this.name = name;
        this.filePath = filePath;

        DecimalFormat f = new DecimalFormat("##.00");
        String strSize = f.format(size);
        if(size < 1)
           strSize = "0" + strSize;
        this.size = String.valueOf(strSize) + " Мб";

        this.coverId = coverId;
    }
}