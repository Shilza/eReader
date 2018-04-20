package com.example.raw.app.Utils;


import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.ParcelFileDescriptor;

import com.example.raw.app.Entities.Book;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class Manager {

    private static final Manager INSTANCE = new Manager();

    public static  Manager getInstance() {
        return INSTANCE;
    }

    private Manager() {
    }

    public void initializeData() {
        try {
            Repository.getInstance().setRecentBooks(FileWorker.getInstance().initializeRecentBooks());
        } catch (FileNotFoundException ex) {
            Repository.getInstance().setRecentBooks(new ArrayList<Book>());
        }
        try {
            Repository.getInstance().setLocalBooks(FileWorker.getInstance().initializeLocalBooks());
        } catch (FileNotFoundException ex) {
            Repository.getInstance().setLocalBooks(BookSearcher.getInstance().localBooksSearching());
        }

        FileWorker.getInstance().refreshingJSON();
        creatingCovers();
    }

    private void creatingCovers() {
        new Thread(new Runnable() {

            private void process(ArrayList<Book> list) {
                for (Book book : list)
                    try {
                        File fl = new File(FileWorker.getInstance().getPicturesPath() + book.getName());
                        if (!fl.exists()) {
                            FileOutputStream out = new FileOutputStream(fl);
                            coverCreation(book.getFilePath()).compress(Bitmap.CompressFormat.PNG, 100, out);
                            out.close();
                        }
                    } catch (Exception e) {
                    }
            }

            @Override
            public void run() {
                process(new ArrayList<>(Repository.getInstance().getRecentBooks()));
                process(new ArrayList<>(Repository.getInstance().getLocalBooks()));
            }
        }).start();
    }

    private Bitmap coverCreation(String filePath) {
        Bitmap bitmap = null;

        try {
            PdfRenderer rend = new PdfRenderer(ParcelFileDescriptor.open(
                    new File(filePath), ParcelFileDescriptor.MODE_READ_ONLY));
            PdfRenderer.Page page = rend.openPage(0);
            bitmap = Bitmap.createBitmap(page.getWidth(), page.getHeight(),
                    Bitmap.Config.ARGB_8888);
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY);
            rend.close();
            page.close();
        } catch (Exception e) {
        }

        return bitmap;
    }
}
