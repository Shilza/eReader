package com.example.raw.app.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Extensions;
import com.example.raw.app.Viewers.PDFViewer;
import com.example.raw.app.Viewers.SimpleTextViewer;

import java.io.File;

public class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    public static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {}

    public void opening(Book book, Context context) {
        if (book.getExtension() == Extensions.PDF) {
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("FilePath", book.getFilePath());
            context.startActivity(intent);
        } else if (isSimpleText(book.getExtension())) {
            Intent intent = new Intent(context, SimpleTextViewer.class);
            intent.putExtra("Text", book.getFilePath());
            context.startActivity(intent);
        }
    }

    public void opening(Book book, int page, Context context) {
        if (book.getExtension() == Extensions.PDF) {
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("FilePath", book.getFilePath());
            intent.putExtra("Page", page);
            context.startActivity(intent);
        }
    }

    public void opening(File file, Extensions extension, Context context) {
        Book book = FileWorker.getInstance().bookPreparing(file, extension);
        for (Extensions ext : Extensions.searchableExtensions())
            if (file.getName().toLowerCase().endsWith(ext.getDescription())) {
                addToRecentBooks(book);
                break;
            }

        opening(book, context);
    }

    private void addToRecentBooks(Book book) {
        boolean isBookExistInList = false;
        for (Book obj : FileWorker.getInstance().getRecentBooks()) {
            if (obj.getFilePath().equals(book.getFilePath())) {
                FileWorker.getInstance().getRecentBooks().remove(obj);
                isBookExistInList = true;
                break;
            }
        }

        if (!isBookExistInList) {
            for (Book obj : FileWorker.getInstance().getLocalBooks()) {
                if (obj.getFilePath().equals(book.getFilePath())) {
                    FileWorker.getInstance().removeBookFromLocalBooks(obj);
                    break;
                }
            }
        }

        FileWorker.getInstance().addToRecentBooks(book);
    }

    private boolean isSimpleText(Extensions extension) {
        for (Extensions ext : Extensions.simpleTextExtensions())
            if (extension == ext)
                return true;

        return false;
    }
}
