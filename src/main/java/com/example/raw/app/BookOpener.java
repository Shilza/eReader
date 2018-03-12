package com.example.raw.app;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {}

    void opening(Book book, Context context){
        if(book.getExtension() == Extensions.PDF){
            Intent intent = new Intent(context, PDFViewer.class);
            intent.putExtra("Book", book);
            context.startActivity(intent);
        } else if(book.getExtension() == Extensions.TXT){
            Intent intent = new Intent(context, TXTViewer.class);
            intent.putExtra("Text", book.getFilePath());
            context.startActivity(intent);
        }

                    /*
            final BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            new FileInputStream(getRealPathFromURI(selectedFile)), "Cp1251"));
            String nextString;
            String finalString = "";
            while ((nextString = br.readLine()) != null) {
                finalString = finalString.concat(nextString);
            }
            */
    }
}
