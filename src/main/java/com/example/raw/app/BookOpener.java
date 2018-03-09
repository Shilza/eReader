package com.example.raw.app;

import android.content.Context;
import android.content.Intent;

class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {

    }

    void opening(Book book, Context context){
        Intent intent = new Intent(context, PDFViewer.class);
        intent.putExtra("Book", book);
        context.startActivity(intent);
    }
}
