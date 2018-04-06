package com.example.raw.app.Utils;


import com.example.raw.app.Entities.Book;

import java.util.ArrayList;

public class Repository {

    private static final Repository INSTANCE = new Repository();
    private ArrayList<Book> recentBooks;
    private ArrayList<Book> localBooks;


    public static Repository getInstance() {
        return INSTANCE;
    }

    private Repository(){
    }

    public ArrayList<Book> getRecentBooks() {
        return recentBooks;
    }

    public ArrayList<Book> getLocalBooks() {
        return localBooks;
    }

    void setRecentBooks(ArrayList<Book> recentBooks) {
        this.recentBooks = recentBooks;
    }

    void setLocalBooks(ArrayList<Book> localBooks) {
        this.localBooks = localBooks;
    }

    public void addToRecentBooks(Book book) {
        if (FileWorker.getInstance().isBookExist(book.getFilePath())) {
            book.setLastActivity(System.currentTimeMillis());
            recentBooks.add(0, book);
            FileWorker.getInstance().refreshingJSON();
        }
    }

    public void addUniqueLocalBooks(ArrayList<Book> books){
        for (Book book : books)
            if (!recentBooks.contains(book) &&
                    !localBooks.contains(book))
                localBooks.add(book);
    }

    public void removeBookFromLocalBooks(Book book) {
        localBooks.remove(book);
        FileWorker.getInstance().refreshingJSON();
    }
}
