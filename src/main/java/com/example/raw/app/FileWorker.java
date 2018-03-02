package com.example.raw.app;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;

class FileWorker{

    private static final String APP_DIRECTORY = Environment.getExternalStorageDirectory()+"/eReader";
    private static final String LIST_RECENT_BOOKS = APP_DIRECTORY + "/recent_books.json";
    private static ArrayList<Book> recentBooks;
    private static ArrayList<Book> localBooks;

    static{
        localBooks = new ArrayList<>();

        try{
            recentBooks = new Gson().fromJson(new BufferedReader(new FileReader(LIST_RECENT_BOOKS)),
                    new TypeToken<ArrayList<Book>>(){}.getType());

            for(Book obj : recentBooks){
                if(!new File(obj.getFilePath()).exists()){
                    recentBooks.remove(obj);
                    refreshingJSON();
                }
            }
        } catch (Exception ex){
            recentBooks = new ArrayList<>();
            ex.printStackTrace();
        } finally{
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
                searchingFiles(Environment.getExternalStorageDirectory());
            //searchingFiles(new File("/storage/")); //SD CARD
        }
    }

    static void checkAppFolder(){
        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        //FileWriter write = new FileWriter(APP_DIRECTORY"/sas.txt");
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
                    String path = entry.getAbsolutePath();

                    float size = entry.length();
                    String strSize;
                    DecimalFormat f = new DecimalFormat("##.00");
                    if(size / 1024 > 1024){
                        size = size / (1024 * 1024);
                        strSize = f.format(size) + " МБ";
                    }
                    else if(size / 1024 > 1){
                        size = size / 1024;
                        strSize = f.format(size) + " КБ";
                    }
                    else
                        strSize = f.format(size) + " Б";


                    Book book = new Book(name, path, strSize, R.drawable.e);

                    Log.d("Saas", entry.getPath());
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

    static void exportToJSON(Book book){
        if(!isBookExistInList(book.getName())){
            recentBooks.add(book);
            refreshingJSON();
            TabRecentBooks.addBook();
        }
    }

    static void refreshingJSON(){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(LIST_RECENT_BOOKS));
            writer.write(new Gson().toJson(recentBooks));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean isBookExistInList(String bookName){
        try{
            for(Book a: recentBooks)
                if(a.getName().equals(bookName))
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
