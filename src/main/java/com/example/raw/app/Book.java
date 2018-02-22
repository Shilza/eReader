package com.example.raw.app;

import java.text.DecimalFormat;

public class Book {
    String name;
    String filePath;
    String size;
    int coverId;

    Book(String name, String filePath, float size, int coverId){
        this.name = name;
        this.filePath = filePath;
        DecimalFormat f = new DecimalFormat("##.00");
        this.size = String.valueOf(f.format(size)) + " Мб";
        this.coverId = coverId;
    }
}
