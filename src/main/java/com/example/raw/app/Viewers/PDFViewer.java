package com.example.raw.app.Viewers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.raw.app.Book;
import com.example.raw.app.FileWorker;
import com.example.raw.app.R;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class PDFViewer extends Activity implements OnPageChangeListener, OnLoadCompleteListener, OnTapListener{
    private PDFView pdfView;
    private ImageButton ibScreenSize;
    private float totalRead = 0;
    private boolean isHorizontalOrientation = false;
    private boolean isFullScreen = false;
    private boolean isExtraMenuHide = false;
    private LinearLayout footer;
    private TextView tvHeader;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        setBook();

        ibScreenSize = findViewById(R.id.action_pdf_viewer_screen_size);
        footer = findViewById(R.id.pdf_view_footer);
        pdfView = findViewById(R.id.pdfView);
        tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText(book.getName());
        pdfView.fromFile(new File(book.getFilePath()))
                .onTap(this)
                .onPageChange(this)
                .swipeHorizontal(isHorizontalOrientation)
                .scrollHandle(new DefaultScrollHandle(this))
                .onLoad(this)
                .load();
    }

    @Override
    public void loadComplete(int pageCount){
        pdfView.jumpTo((int)(pageCount*totalRead));
    }

    @Override
    public boolean onTap(MotionEvent e){
        animations();
        return true;
    }

    private void animations(){
        footerAnimation(isExtraMenuHide);
        tvHeaderAnimation(isExtraMenuHide);
        isExtraMenuHide = !isExtraMenuHide;
    }

    //HARD METHOD
    @Override
    public void onPageChanged(int curPage, int count){
        book.setTotalRead((float)curPage/(float)count);
        FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight() ;
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void tvHeaderAnimation(boolean show){
        int value = show ? tvHeader.getHeight() : -tvHeader.getHeight();
        tvHeader.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
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

                animations();
                pdfView.recycle();
                pdfView.fromFile(new File(book.getFilePath()))
                        .onTap(this)
                        .defaultPage(pdfView.getCurrentPage())
                        .onPageChange(this)
                        .swipeHorizontal(isHorizontalOrientation)
                        .scrollHandle(new DefaultScrollHandle(this))
                        .load();

                break;

            case R.id.action_pdf_viewer_screen_size:
                isFullScreen=!isFullScreen;

                animations();
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
