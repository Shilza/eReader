package com.example.raw.app;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class PDFViewer extends Activity implements OnPageChangeListener, OnLoadCompleteListener{
    private PDFView pdfView;
    private ImageButton ibScreenSize;
    private float totalRead = 0;
    private boolean isHorizontalOrientation = false;
    private boolean isFullScreen = false;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        setBook();

        ibScreenSize = findViewById(R.id.action_pdf_viewer_screen_size);
        pdfView = findViewById(R.id.pdfView);
        ((TextView)findViewById(R.id.tv_header)).setText(book.getName());
        pdfView.fromFile(new File(book.getFilePath()))
                .onPageChange(this)
                .swipeHorizontal(isHorizontalOrientation)
                .scrollHandle(new DefaultScrollHandle(this))
                .onLoad(this)
                .load();

    }

    @Override
    public void loadComplete(int pageCount){
        pdfView.setPositionOffset(totalRead+pageCount/500);
    }

    //HARD METHOD
    @Override
    public void onPageChanged(int curPage, int count){
        pdfView.bit
        //book.setTotalRead((float)curPage/(float)count);
        //FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
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
                isHorizontalOrientation =!isHorizontalOrientation;

                pdfView.recycle();
                pdfView.fromFile(new File(book.getFilePath()))
                        .defaultPage(pdfView.getCurrentPage())
                        .onPageChange(this)
                        .swipeHorizontal(isHorizontalOrientation)
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
                            WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
                break;

            case R.id.action_pdf_viewer_bookmark:
                break;
        }
    }

    private void setBook(){
        String filePath = String.valueOf(getIntent().getSerializableExtra("Book"));
        for(Book obj : FileWorker.getInstance().getRecentBooks())
            if(obj.getFilePath().equals(filePath)){
                book = obj;
                totalRead = book.getTotalRead();
                break;
            }
    }
}
