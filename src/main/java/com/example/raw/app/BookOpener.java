package com.example.raw.app;

import android.content.Context;
import android.content.Intent;

import com.example.raw.app.Viewers.PDFViewer;
import com.example.raw.app.Viewers.SimpleTextViewer;

class BookOpener {
    private static final BookOpener INSTANCE = new BookOpener();

    static BookOpener getInstance() {
        return INSTANCE;
    }

    private BookOpener() {}

    private boolean sas(Extensions extension){
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
        } else if(sas(book.getExtension())){
            Intent intent = new Intent(context, SimpleTextViewer.class);
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
