package com.example.raw.app.Utils;

import android.content.Context;
import android.content.Intent;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Extensions;
import com.example.raw.app.Viewers.PDFViewer;
import com.example.raw.app.Viewers.SimpleTextViewer;

public class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    public static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {}

    private boolean isSimpleText(Extensions extension){
        for(Extensions ext : Extensions.simpleTextExtensions())
            if(extension == ext)
                return true;

        return false;
    }

    public void opening(Book book, Context context){
        if(book.getExtension() == Extensions.PDF){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("FilePath", book.getFilePath());
            context.startActivity(intent);
        } else if(isSimpleText(book.getExtension())){
            Intent intent = new Intent(context, SimpleTextViewer.class);
            intent.putExtra("Text", book.getFilePath());
            context.startActivity(intent);
        }
    }

    public void opening(Book book, int page, Context context){
        if(book.getExtension() == Extensions.PDF){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("FilePath", book.getFilePath());
            intent.putExtra("Page", page);
            context.startActivity(intent);
        }
    }
}
