package com.example.raw.app.Viewers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.raw.app.BookmarksOfParticularBookActivity;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.Entities.Bookmark;
import com.example.raw.app.Viewers.Dialogs.BookmarksDialog;
import com.example.raw.app.Viewers.Dialogs.GoToDialog;
import com.example.raw.app.Utils.FileWorker;
import com.example.raw.app.R;
import com.example.raw.app.TabsKeeper;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class PDFViewer extends Activity
        implements OnPageChangeListener,OnLoadCompleteListener, OnTapListener, GoToDialog.OnInputListener {

    private PDFView pdfView;
    private ImageButton ibScreenSize;
    private ImageButton ibBookmarks;
    private TextView tvBookmarksCount;
    private float totalRead = 0;
    private boolean isHorizontalOrientation = false;
    private boolean isFullScreen = false;
    private boolean isExtraMenuHide = false;
    private View footer;
    private View header;
    private int startPage;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        setBook();

        header = findViewById(R.id.pdf_viewer_header);
        ibScreenSize = findViewById(R.id.action_pdf_viewer_screen_size);
        footer = findViewById(R.id.pdf_view_footer);
        ibBookmarks = findViewById(R.id.action_pdf_viewer_bookmarks);
        tvBookmarksCount = findViewById(R.id.action_pdf_viewer_bookmarks_count);
        pdfView = findViewById(R.id.pdfView);
        ((TextView)findViewById(R.id.pdf_viewer_tv_header)).setText(book.getName());

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
        if(startPage == -1)
            pdfView.jumpTo((int)(pageCount*totalRead), true);
        else
            pdfView.jumpTo(startPage, true);
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
        refreshBooksData();

        int bookmarksCount = 0;
        for(Bookmark bookmark : book.getBookmarks())
            if(bookmark.getPage() == curPage)
                bookmarksCount++;

        if(bookmarksCount > 0){
            ibBookmarks.setImageResource(R.drawable.ic_bookmark_border_red_28dp);
            tvBookmarksCount.setText(String.valueOf(bookmarksCount));
            tvBookmarksCount.setVisibility(View.VISIBLE);
        }
        else{
            ibBookmarks.setImageResource(R.drawable.ic_bookmark_border_white_28dp);
            tvBookmarksCount.setVisibility(View.GONE);
        }
    }

    private void refreshBooksData(){
        book.setLastActivity(new Date().getTime());
        FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_VOLUME_UP){
            pdfView.jumpTo(pdfView.getCurrentPage()-1, true);
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_VOLUME_DOWN){
            pdfView.jumpTo(pdfView.getCurrentPage()+1, true);
            return true;
        }
        else if(keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        refreshBooksData();
        TabsKeeper.getInstance().notifyDataSetChanged();
        finish();
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
                Intent intent = new Intent(this, BookmarksOfParticularBookActivity.class);
                intent.putExtra("FilePath", book.getFilePath());
                intent.putExtra("Bookmarks", getBookmarks());
                startActivity(intent);
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

    private ArrayList<Bookmark> getBookmarks(){
        ArrayList<Bookmark> bookmarks = new ArrayList<>();

        for(Bookmark bookmark : book.getBookmarks())
            if(bookmark.getPage() == pdfView.getCurrentPage())
                bookmarks.add(bookmark);

        if(bookmarks.size() == 0)
            bookmarks = book.getBookmarks();

        return bookmarks;
    }

    private void setBook(){
        String filePath = String.valueOf(getIntent().getSerializableExtra("FilePath"));
        startPage = getIntent().getIntExtra("Page", -1);

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
