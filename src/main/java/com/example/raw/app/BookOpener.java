package com.example.raw.app;

import android.content.Context;
import android.content.Intent;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Viewers.PDFViewer;
import com.example.raw.app.Viewers.SimpleTextViewer;

class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {}

    private boolean isSimpleText(Extensions extension){
        for(Extensions ext : Extensions.simpleTextExtensions())
            if(extension == ext)
                return true;

        return false;
    }

    void opening(Book book, Context context){
        if(book.getExtension() == Extensions.PDF){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("Book", book.getFilePath());
            context.startActivity(intent);
        } else if(isSimpleText(book.getExtension())){
            Intent intent = new Intent(context, SimpleTextViewer.class);
            intent.putExtra("Text", book.getFilePath());
            context.startActivity(intent);
        }

    }
}
