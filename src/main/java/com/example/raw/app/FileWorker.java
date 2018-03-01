package com.example.raw.app;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

class FileWorker{

    private static final String APP_DIRECTORY = Environment.getExternalStorageDirectory ()+"/eReader";
    private static final String LIST_RECENT_BOOKS = APP_DIRECTORY + "/recent_books.json";
    static ArrayList<Book> books;

    static{
        try{
            books = new Gson().fromJson(new BufferedReader(new FileReader(LIST_RECENT_BOOKS)),
                    new TypeToken<ArrayList<Book>>(){}.getType());
        }catch (Exception ex){
            books = new ArrayList<>();
            ex.printStackTrace();
        }
    }

    static void checkAppFolder(){
        File directory = new File(APP_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
        }
        //FileWriter write = new FileWriter(APP_DIRECTORY"/sas.txt");
    }

     static ArrayList<Book> initializeLocalBooksData(ContentResolver contentResolver){
        ArrayList<Book> books = new ArrayList<>();

        Uri uri = MediaStore.Files.getContentUri("external");

        String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE + "=?";
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf");
        String[] selectionArgsPdf = new String[]{ mimeType };
        Cursor cursor = contentResolver.query(uri, null, selectionMimeType, selectionArgsPdf,null);// cursor -- allPDFFiles

        if (cursor != null){
            while (cursor.moveToNext()){
                if(cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME)) != null){
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME));
                    String path = cursor.getString(cursor.getColumnIndex(MediaStore.Files.FileColumns.DATA));
                    float size = cursor.getFloat(cursor.getColumnIndex(MediaStore.Files.FileColumns.SIZE)) / (1024*1024);
                    String extension = ".pdf";
                    if(name.contains(extension))
                        name = name.substring(0, name.indexOf(extension));
                        Book book = new Book(name, path, size, R.drawable.e);
                        books.add(book);
                }
            }
            cursor.close();
        }

        return books;
    }

    static ArrayList<Book> getBooks(){
            return books;
        }

    static void exportToJSON(Book book){
        if(!isBookExist(book.getName())){
            books.add(book);

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(LIST_RECENT_BOOKS));
                writer.write(new Gson().toJson(books));
                writer.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            TabRecentBooks.addBook();
        }
    }

    static boolean isBookExist(String bookName){
        try{
            for(Book a: books)
                if(a.getName().equals(bookName))
                    return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }
}
