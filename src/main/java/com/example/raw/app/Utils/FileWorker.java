package com.example.raw.app.Utils;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Extensions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;

public class FileWorker{

    private static final FileWorker INSTANCE = new FileWorker();
    private final String APP_DIRECTORY = Environment.getExternalStorageDirectory() + "/eReader/";
    private final String PICTURES = APP_DIRECTORY + "Pictures/";
    private final String LIST_RECENT_BOOKS = APP_DIRECTORY + "recent_books.json";
    private final String LIST_LOCAL_BOOKS = APP_DIRECTORY + "local_books.json";
    private ArrayList<Book> recentBooks;
    private ArrayList<Book> localBooks;

    public static FileWorker getInstance() {
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
            localBooksSearching();
        }

        creatingCovers();
    }

    private void creatingCovers(){
        new Thread(new Runnable() {

            private void process(ArrayList<Book> list){

                for(Book book : list){

                    File file = new File(PICTURES);
                    if(!file.exists()) {
                        file.mkdir();
                        try{
                            new File(PICTURES + ".nomedia").createNewFile();
                        } catch (Exception e){}

                    }
                    else
                        try {
                            File fl = new File(PICTURES + book.getName() + ".png");
                            if (!fl.exists()) {
                                FileOutputStream out = new FileOutputStream(fl);
                                coverCreation(book.getFilePath()).compress(Bitmap.CompressFormat.PNG, 100, out);
                                out.close();
                            }
                        } catch (Exception e) {}
                }
            }

            @Override
            public void run() {
                process(new ArrayList<>(recentBooks));
                process(new ArrayList<>(localBooks));
            }
        }).start();
    }

    private Bitmap coverCreation(String filePath){
        Bitmap bitmap = null;

        try{
            PdfRenderer rend = new PdfRenderer(ParcelFileDescriptor.open(new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
            PdfRenderer.Page page = rend.openPage(0);
            bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                    Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            rend.close();
            page.close();
        }
        catch(Exception e){}

        return bitmap;
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
            else
                for(Extensions ext : Extensions.searchableExtensions())
                    if(dir.getName().contains(ext.getDescription())){
                        bookEntry(bookPreparing(dir));
                        break;
                    }
        }
    }

    public void localBooksSearching(){
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            for(File f : new File("/storage").listFiles())
                if(f.getName().contains("sdcard"))
                    searchingFiles(f);
        }

        refreshingJSON(localBooks);
    }

    private void bookEntry(Book book){
        if(!recentBooks.contains(book) && !localBooks.contains(book))
            localBooks.add(book);
    }

    public Book bookPreparing(File directory){
        String temp = directory.getName();
        Extensions extension = null;

        for(Extensions ext : Extensions.values())
            if(temp.toLowerCase().contains(ext.getDescription())){
                extension = ext;
                break;
            }

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
        else
            strSize = String.valueOf(size) + "Б";

        return new Book(name, filePath, strSize, lastActivity, extension);
    }

    public ArrayList<Book> getLocalBooks(){
        return localBooks;
    }

    public ArrayList<Book> getRecentBooks(){
        return recentBooks;
    }

    public String getPicturesPath(){
        return PICTURES;
    }

    public void addingToRecentBooks(Book book){
        if(isBookExist(book.getFilePath())){
            book.setLastActivity(new Date().getTime());
            recentBooks.add(0, book);
            refreshingJSON(recentBooks);
        }
    }

    public void removeBookFromLocalBooks(Book book){
        localBooks.remove(book);
        refreshingJSON(localBooks);
    }

    public void refreshingJSON(ArrayList<Book> books){
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

    public boolean isBookExist(String bookPath){
        return (new File(bookPath).exists());
    }
}