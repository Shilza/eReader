package com.example.raw.app.Utils;

import android.os.Environment;
import android.util.Log;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Extensions;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class BookSearcher {

    private static final BookSearcher INSTANCE = new BookSearcher();

    public static BookSearcher getInstance() {
        return INSTANCE;
    }

    private ArrayList<Book> books;

    private BookSearcher() {
        books = new ArrayList<>();
    }

    public ArrayList<Book> localBooksSearching() {

        //internal memory search
        searchingFiles(Environment.getExternalStorageDirectory());

        //SD card search
        for (File f : new File("/storage").listFiles()) {
            if (f.listFiles() != null && f.getName().contains("sdcard1"))
                searchingFiles(f);
        }

        return new ArrayList<>(books);
    }

    private void searchingFiles(File directory) {
        for (File dir : directory.listFiles()) {
            if (dir.isDirectory())
                searchingFiles(dir);
            else
                try {
                    books.add(bookPreparing(dir));
                } catch (IllegalArgumentException e) {
                }
        }
    }

    Book bookPreparing(File directory) throws IllegalArgumentException {
        String temp = directory.getName();

        for (Extensions ext : Extensions.searchableExtensions())
            if (temp.toLowerCase().endsWith(ext.getDescription())) {

                String name = temp.substring(0, temp.toLowerCase().indexOf(ext.getDescription()));

                String filePath = directory.getAbsolutePath();
                long lastActivity = directory.lastModified();

                float size = directory.length();
                String strSize;
                DecimalFormat f = new DecimalFormat("##.00");
                if (size / 1024 > 1024) {
                    size = size / (1024 * 1024);
                    strSize = f.format(size) + " MB";
                } else if (size > 1024) {
                    size = size / 1024;
                    strSize = f.format(size) + " KB";
                } else
                    strSize = String.valueOf(size) + " B";

                return new Book(name, filePath, strSize, lastActivity, ext);
            }

        throw new IllegalArgumentException();
    }
}
