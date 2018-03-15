package com.example.raw.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;

import java.io.File;
import java.util.List;

public class PDFViewer extends Activity implements OnPageChangeListener, OnLoadCompleteListener{
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    private ImageButton ibScreenSize;
    private boolean isVerticalOrientation = false;
    private boolean isFullScreen = false;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);

        ibScreenSize = findViewById(R.id.action_pdf_viewer_screen_size);
        pdfView = findViewById(R.id.pdfView);
        book = (Book)getIntent().getSerializableExtra("Book");
        ((TextView)findViewById(R.id.tv_header)).setText(book.getName());
        pdfView.fromFile(new File(book.getFilePath())).swipeHorizontal(true).scrollHandle(new DefaultScrollHandle(this)).load();
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

    public void pdfViewerOnClick(View view){
        switch (view.getId()){
            case R.id.action_pdf_viewer_share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/*");
                intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + book.getFilePath()));
                startActivity(Intent.createChooser(intent, "Share with"));
                break;
            case R.id.action_pdf_viewer_orientation:
                isVerticalOrientation=!isVerticalOrientation;

                pdfView.recycle();
                pdfView.fromFile(new File(book.getFilePath()))
                        .swipeHorizontal(isVerticalOrientation)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();

                break;
            case R.id.action_pdf_viewer_screen_size:
                isFullScreen=!isFullScreen;
                if(isFullScreen){
                    ibScreenSize.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                else{
                    ibScreenSize.setImageResource(R.drawable.ic_fullscreen_black_24dp);
                    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                            WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                break;
            case R.id.action_pdf_viewer_bookmark:
                break;
        }
    }
}
