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

import com.example.raw.app.BookmarksOfParticularBookActivity;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.Viewers.Dialogs.BookmarksDialog;
import com.example.raw.app.Viewers.Dialogs.GoToDialog;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.R;
import com.example.raw.app.TabKeeper;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.Date;

public class PDFViewer extends Activity
        implements OnPageChangeListener,OnLoadCompleteListener, OnTapListener, GoToDialog.OnInputListener {

    private PDFView pdfView;
    private ImageButton ibScreenSize;
    private float totalRead = 0;
    private boolean isHorizontalOrientation = false;
    private boolean isFullScreen = false;
    private boolean isExtraMenuHide = false;
    private LinearLayout footer;
    private LinearLayout header;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        setBook();

        header = findViewById(R.id.pdf_viewer_header);
        ibScreenSize = findViewById(R.id.action_pdf_viewer_screen_size);
        footer = findViewById(R.id.pdf_view_footer);
        pdfView = findViewById(R.id.pdfView);
        TextView tvHeader = findViewById(R.id.pdf_viewer_tv_header);
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
        pdfView.jumpTo((int)(pageCount*totalRead), true);
    }

    @Override
    public boolean onTap(MotionEvent e){
        animations();
        return true;
    }

    @Override
    public void sendInput(int value){
        pdfView.jumpTo(value);
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
        book.setLastActivity(new Date().getTime());
        FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
        TabKeeper.getInstance().notifyDataSetChanged();
    }

    private void footerAnimation(boolean show){
        int value = show ? -footer.getHeight() : footer.getHeight() ;
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void tvHeaderAnimation(boolean show){
        int value = show ? header.getHeight() : -header.getHeight();
        header.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    public void pdfViewerOnClick(View view){
        switch (view.getId()){
            case R.id.action_pdf_viewer_bookmarks:
                startActivity(new Intent(this, BookmarksOfParticularBookActivity.class));
                break;

            case R.id.action_pdf_viewer_share:
                bookSharing();
                break;

            case R.id.action_pdf_viewer_orientation:
                orientationChanging();
                break;

            case R.id.action_pdf_viewer_goto:
                createGoToDialog();
                break;

            case R.id.action_pdf_viewer_screen_size:
                sizeChanging();
                break;

            case R.id.action_pdf_viewer_add_bookmarks:
                createBookmarksDialog();
                break;
        }
    }

    private void bookSharing(){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + book.getFilePath()));
        startActivity(Intent.createChooser(intent, "Share with"));
    }

    private void orientationChanging(){
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
    }

    private void createGoToDialog(){
        Bundle args = new Bundle();
        args.putInt("maxPageCount", pdfView.getPageCount());
        args.putInt("currentPage", pdfView.getCurrentPage());
        GoToDialog goToDialog = new GoToDialog();
        goToDialog.setArguments(args);
        goToDialog.show(getFragmentManager(), "GoToDialog");
    }

    private void createBookmarksDialog(){
        Bundle args = new Bundle();
        args.putInt("currentPage", pdfView.getCurrentPage());
        args.putSerializable("book", book);
        BookmarksDialog dialog = new BookmarksDialog();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "BookmarksDialog");
    }

    private void sizeChanging(){
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
    }

    private void setBook(){
        String filePath = String.valueOf(getIntent().getSerializableExtra("Book"));
        for(Book obj : FileWorker.getInstance().getRecentBooks())
            if(obj.getFilePath().equals(filePath)){
                book = obj;
                totalRead = book.getTotalRead();
                break;
            }
        if(book == null)
            for(Book obj : FileWorker.getInstance().getLocalBooks())
                if(obj.getFilePath().equals(filePath)){
                    book = obj;
                    totalRead = book.getTotalRead();
                    break;
                }
    }
}
