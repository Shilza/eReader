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
            try{
                Intent intent = new Intent(context, TXTViewer.class);
                String text = getString(book.getFilePath());
                intent.putExtra("Text", text);
                context.startActivity(intent);
            } catch (IOException ex){
                Toast.makeText(context, "Невозможо открыть файл", Toast.LENGTH_SHORT).show();
            }
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

    private static String getString(String filePath) throws IOException {
        BufferedReader reader = new BufferedReader( new FileReader(filePath));

        String line;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        while((line = reader.readLine()) != null){
            stringBuilder.append(line);
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }
}
