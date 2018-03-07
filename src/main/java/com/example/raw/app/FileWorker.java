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

    private static final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + "/eReader";
    private static final String LIST_RECENT_BOOKS = APP_DIRECTORY + "/recent_books.json";
    private static final String LIST_LOCAL_BOOKS = APP_DIRECTORY + "/local_books.json";
    private static ArrayList<Book> recentBooks;
    private static ArrayList<Book> localBooks;

    static{
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

    private static ArrayList<Book> initializeData(ArrayList<Book> books) throws Exception{

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

    static void checkAppFolder(){
        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
    }

    private static void searchingFiles(File folder) {
        File[] folderEntries = folder.listFiles();

        for (File entry : folderEntries){
            if (entry.isDirectory())
                searchingFiles(entry);
            else{
                String temp = entry.getName();
                String extension = ".pdf";
                int index = temp.indexOf(extension);

                if(index != -1){
                    String name = temp.substring(0, temp.indexOf(extension));
                    String filePath = entry.getAbsolutePath();
                    long lastActivity = entry.lastModified();

                    float size = entry.length();
                    String strSize;
                    DecimalFormat f = new DecimalFormat("##.00");
                    if(size / 1024 > 1024){
                        size = size / (1024 * 1024);
                        strSize = f.format(size) + " МБ";
                    }
                    else{
                        size = size / 1024;
                        strSize = f.format(size) + " КБ";
                    }

                    Book book = new Book(name, filePath, strSize, lastActivity);

                    boolean isBookContains = false;
                    for(Book obj : recentBooks){
                        if(obj.equals(book)){
                            isBookContains = true;
                            break;
                        }
                    }
                    if(!isBookContains)
                        localBooks.add(book);
                }

            }
        }
    }

    static ArrayList<Book> getLocalBooks(){
        return localBooks;
    }

    static ArrayList<Book> getRecentBooks(){
            return recentBooks;
    }

    static void exportRecentBooksToJSON(Book book){
        if(isBookExist(book.getFilePath()) && !isBookExistInList(book)){
            book.setLastActivity(new Date().getTime());
            recentBooks.add(0, book);
            localBooks.remove(book);
            refreshingJSON(recentBooks);
            refreshingJSON(localBooks);
        }
    }

    static void refreshingJSON(ArrayList<Book> books){
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

    private static boolean isBookExistInList(Book book){
        try{
            for(Book obj: recentBooks)
                if(obj.equals(book))
                    return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }

        return false;
    }

    static boolean isBookExist(String bookPath){
        return (new File(bookPath).exists());
    }
}
