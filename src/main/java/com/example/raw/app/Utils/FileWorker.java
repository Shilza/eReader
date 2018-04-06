package com.example.raw.app.Utils;

import android.os.Environment;

import com.example.raw.app.Entities.Book;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class FileWorker {

    private static final FileWorker INSTANCE = new FileWorker();

    private final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + "/" + "Silicate Reader" + "/"; //REMAKE WITH STRINGS.XML
    private final String PICTURES = APP_DIRECTORY + "Pictures/";
    private final String LIST_RECENT_BOOKS = APP_DIRECTORY + "recent_books";
    private final String LIST_LOCAL_BOOKS = APP_DIRECTORY + "local_books";

    public static FileWorker getInstance() {
        return INSTANCE;
    }

    private FileWorker() {
        makeAppDirectory();
    }

    public String getPicturesPath() {
        return PICTURES;
    }

    public void refreshingJSON() {
        try {
            BufferedWriter writer;

            writer = new BufferedWriter(new FileWriter(LIST_RECENT_BOOKS));
            writer.write(new Gson().toJson(Repository.getInstance().getRecentBooks()));
            writer.close();

            BufferedWriter writer1;
            writer1 = new BufferedWriter(new FileWriter(LIST_LOCAL_BOOKS));
            writer1.write(new Gson().toJson(Repository.getInstance().getLocalBooks()));
            writer1.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean isBookExist(String bookPath) {
        return (new File(bookPath).exists());
    }

    ArrayList<Book> initializeRecentBooks() throws FileNotFoundException{
        ArrayList<Book> books = new Gson().fromJson(new BufferedReader(
                        new FileReader(LIST_RECENT_BOOKS)),
                new TypeToken<ArrayList<Book>>() {
                }.getType());

        deleteUnessentialBooks(books);
        return books;
    }

    ArrayList<Book> initializeLocalBooks() throws FileNotFoundException{
        ArrayList<Book> books = new Gson().fromJson(new BufferedReader(
                        new FileReader(LIST_LOCAL_BOOKS)),
                new TypeToken<ArrayList<Book>>() {
                }.getType());

        deleteUnessentialBooks(books);
        return books;
    }

    private void deleteUnessentialBooks(ArrayList<Book> books){
        ArrayList<Book> deletedBooks = new ArrayList<>();
        for (Book obj : books)
            if (!new File(obj.getFilePath()).exists())
                deletedBooks.add(obj);
        if (!deletedBooks.isEmpty()) {
            books.removeAll(deletedBooks);
            refreshingJSON();
        }
    }

    private void makeAppDirectory() {
        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    void makePicturesDirectory() {
        File file = new File(PICTURES);
        if (!file.exists()) {
            file.mkdir();
            try {
                new File(PICTURES + ".nomedia").createNewFile();
            } catch (Exception e) {
            }
        }
    }
}
