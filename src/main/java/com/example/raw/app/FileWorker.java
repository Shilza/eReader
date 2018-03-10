package com.example.raw.app;

import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

class FileWorker{

    private static final FileWorker INSTANCE = new FileWorker();
    private final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + "/eReader";
    private final String LIST_RECENT_BOOKS = APP_DIRECTORY + "/recent_books.json";
    private final String LIST_LOCAL_BOOKS = APP_DIRECTORY + "/local_books.json";
    private ArrayList<Book> recentBooks;
    private ArrayList<Book> localBooks;

    static FileWorker getInstance() {
        return INSTANCE;
    }

    private FileWorker() {
        checkAppDirectory();
        recentBooks = new ArrayList<>();
        localBooks = new ArrayList<>();

        try{
            recentBooks = initializeData(recentBooks);
        } catch (Exception ex){}

        try{
            localBooks = initializeData(localBooks);
        } catch (Exception ex){
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                searchingFiles(Environment.getExternalStorageDirectory());

            refreshingJSON(localBooks);
        }
        //searchingFiles(new File("/storage/")); //SD CARD
    }

    private ArrayList<Book> initializeData(ArrayList<Book> books) throws Exception{

        books = new Gson().fromJson(new BufferedReader(
                        new FileReader(books == recentBooks ? LIST_RECENT_BOOKS : LIST_LOCAL_BOOKS)),
                new TypeToken<ArrayList<Book>>(){}.getType());

        ArrayList<Book> deletedBooks = new ArrayList<>();
        for(Book obj : books)
            if(!new File(obj.getFilePath()).exists())
                deletedBooks.add(obj);
         if(!deletedBooks.isEmpty()){
             books.removeAll(deletedBooks);
             refreshingJSON(books);
         }

         return books;
    }

    private void checkAppDirectory(){
        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private void searchingFiles(File directory) {
        File[] folderEntries = directory.listFiles();

        for (File dir: folderEntries){
            if (dir.isDirectory())
                searchingFiles(dir);
            else{
                for(Extensions ext : Extensions.searchableExtensions())
                    if(dir.getName().contains(ext.getDescription()))
                        bookEntry(bookPreparing(dir));
            }
        }
    }

    void bookEntry(Book book){
        if(book != null && !recentBooks.contains(book))
            localBooks.add(book);
    }

    Book bookPreparing(File directory){
        String temp = directory.getName();
        Extensions extension = null;

        for(Extensions ext : Extensions.values())
            if(temp.contains(ext.getDescription()))
                extension = ext;

        String name = temp.substring(0, temp.indexOf(extension.getDescription()));
        String filePath = directory.getAbsolutePath();
        long lastActivity = directory.lastModified();

        float size = directory.length();
        String strSize;
        DecimalFormat f = new DecimalFormat("##.00");
        if(size / 1024 > 1024){
            size = size / (1024 * 1024);
            strSize = f.format(size) + " МБ";
        }
        else if(size > 1024){
            size = size / 1024;
            strSize = f.format(size) + " КБ";
        }
        else{
            strSize = String.valueOf(size) + "Б";
        }

        return new Book(name, filePath, strSize, lastActivity, extension);
    }

    ArrayList<Book> getLocalBooks(){
        return localBooks;
    }

    ArrayList<Book> getRecentBooks(){
            return recentBooks;
    }

    void addingToRecentBooks(Book book){
        if(isBookExist(book.getFilePath())){
            book.setLastActivity(new Date().getTime());
            recentBooks.add(0, book);
            refreshingJSON(recentBooks);
        }
    }

    void removeBookFromLocalBooks(Book book){
        localBooks.remove(book);
        refreshingJSON(localBooks);
    }

    void refreshingJSON(ArrayList<Book> books){
        try {
            BufferedWriter writer;
            if(books == recentBooks)
                writer = new BufferedWriter(new FileWriter(LIST_RECENT_BOOKS));
            else if(books == localBooks)
                writer = new BufferedWriter(new FileWriter(LIST_LOCAL_BOOKS));
            else
                return;

            writer.write(new Gson().toJson(books));
            writer.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    boolean isBookExist(String bookPath){
        return (new File(bookPath).exists());
    }
}
