package com.example.raw.app;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class PDFViewer extends Activity implements OnPageChangeListener,OnLoadCompleteListener{
    public static String SAMPLE_FILE;
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        pdfView= findViewById(R.id.pdfView);
        SAMPLE_FILE = getIntent().getSerializableExtra("Book").toString();
        pdfView.fromFile(new File(SAMPLE_FILE)).load();
        //displayFromAsset(SAMPLE_FILE);
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }


    @Override
    public void loadComplete(int nbPages) {
        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {
            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}

