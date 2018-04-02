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

import com.example.raw.app.Viewers.Activities.BookmarksOfParticularBookActivity;
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
        implements OnPageChangeListener, OnLoadCompleteListener, OnTapListener, GoToDialog.OnInputListener {

    private PDFView pdfView;
    private Book book;

    private ImageButton ibScreenSize;
    private ImageButton ibBookmarks;
    private TextView tvBookmarksCount;
    private View footer;
    private View header;

    private float totalRead = 0;
    private boolean isHorizontalOrientation = false;
    private boolean isFullScreen = false;
    private boolean isExtraMenuHide = false;

    private int startPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        setBook();

        header = findViewById(R.id.acPDFViewerHeader);
        ibScreenSize = findViewById(R.id.acPDFViewerActionChangeScreenSize);
        footer = findViewById(R.id.acPDFViewerFooter);
        ibBookmarks = findViewById(R.id.acPDFViewerActionBookmarks);
        tvBookmarksCount = findViewById(R.id.acPDFViewerTvBookmarksCount);
        pdfView = findViewById(R.id.pdfView);
        ((TextView) findViewById(R.id.acPDFViewerTvHeader)).setText(book.getName());

        pdfView.fromFile(new File(book.getFilePath()))
                .onTap(this)
                .onPageChange(this)
                .swipeHorizontal(isHorizontalOrientation)
                .scrollHandle(new DefaultScrollHandle(this))
                .onLoad(this)
                .load();
    }

    @Override
    public void loadComplete(int pageCount) {
        if (startPage == -1)
            pdfView.jumpTo((int) (pdfView.getPageCount() * totalRead), true);
        else
            pdfView.jumpTo(startPage, true);
    }

    @Override
    public boolean onTap(MotionEvent e) {
        animations();
        return true;
    }

    @Override
    public void sendInput(int value) {
        pdfView.jumpTo(value);
    }

    private void animations() {
        footerAnimation(isExtraMenuHide);
        tvHeaderAnimation(isExtraMenuHide);
        isExtraMenuHide = !isExtraMenuHide;
    }

    @Override
    public void onPageChanged(int curPage, int count) {
        book.setTotalRead((float) curPage / (float) count);
        refreshBooksData();
        bookmarksProcessing(curPage);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            pdfView.jumpTo(pdfView.getCurrentPage() - 1, true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            pdfView.jumpTo(pdfView.getCurrentPage() + 1, true);
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
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

    public void pdfViewerOnClick(View view) {
        switch (view.getId()) {
            case R.id.acPDFViewerActionBookmarks:
                Intent intent = new Intent(this, BookmarksOfParticularBookActivity.class);
                intent.putExtra("IndexesOfBookmarks", getBookmarks());
                intent.putExtra("IndexInRecentBooks", FileWorker.getInstance().getRecentBooks().indexOf(book));
                startActivity(intent);
                break;

            case R.id.acPDFViewerActionShare:
                bookSharing();
                break;

            case R.id.acPDFViewerActionChangeOrientation:
                orientationChanging();
                break;

            case R.id.acPDFViewerActionGoto:
                createGoToDialog();
                break;

            case R.id.acPDFViewerActionChangeScreenSize:
                sizeChanging();
                break;

            case R.id.acPDFViewerActionAddBookmarks:
                createBookmarksDialog();
                break;
        }
    }

    private void footerAnimation(boolean show) {
        int value = show ? -footer.getHeight() : footer.getHeight();
        footer.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void tvHeaderAnimation(boolean show) {
        int value = show ? header.getHeight() : -header.getHeight();
        header.animate().translationYBy(value).setDuration(200).setInterpolator(new AccelerateInterpolator()).start();
    }

    private void bookmarksProcessing(int curPage){
        int bookmarksCount = 0;
        for (Bookmark bookmark : book.getBookmarks())
            if (bookmark.getPage() == curPage)
                bookmarksCount++;

        if (bookmarksCount > 0) {
            ibBookmarks.setImageResource(R.drawable.ic_bookmark_border_red_28dp);
            tvBookmarksCount.setText(String.valueOf(bookmarksCount));
            tvBookmarksCount.setVisibility(View.VISIBLE);
        } else {
            ibBookmarks.setImageResource(R.drawable.ic_bookmark_border_white_28dp);
            tvBookmarksCount.setVisibility(View.GONE);
        }
    }

    private void refreshBooksData() {
        book.setLastActivity(new Date().getTime());
        FileWorker.getInstance().refreshingJSON(FileWorker.getInstance().getRecentBooks());
    }

    private void bookSharing() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + book.getFilePath()));
        startActivity(Intent.createChooser(intent, getString(R.string.intent_share)));
    }

    private void orientationChanging() {
        isHorizontalOrientation = !isHorizontalOrientation;

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

    private void createGoToDialog() {
        Bundle args = new Bundle();
        args.putInt("maxPageCount", pdfView.getPageCount());
        args.putInt("currentPage", pdfView.getCurrentPage());
        GoToDialog goToDialog = new GoToDialog();
        goToDialog.setArguments(args);
        goToDialog.show(getFragmentManager(), "GoToDialog");
    }

    private void createBookmarksDialog() {
        Bundle args = new Bundle();
        args.putInt("currentPage", pdfView.getCurrentPage());
        args.putSerializable("IndexInRecentBooks", FileWorker.getInstance().getRecentBooks().indexOf(book));
        BookmarksDialog dialog = new BookmarksDialog();
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "BookmarksDialog");
    }

    private void sizeChanging() {
        isFullScreen = !isFullScreen;

        animations();
        if (isFullScreen) {
            ibScreenSize.setImageResource(R.drawable.ic_fullscreen_exit_black_24dp);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } else {
            ibScreenSize.setImageResource(R.drawable.ic_fullscreen_black_24dp);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    private ArrayList<Integer> getBookmarks() {
        ArrayList<Integer> bookmarks = new ArrayList<>();

        for(int i = 0; i < book.getBookmarks().size(); i++)
            if (book.getBookmarks().get(i).getPage() == pdfView.getCurrentPage())
                bookmarks.add(i);

        if (bookmarks.size() == 0)
            for(int i=0; i<book.getBookmarks().size();i++)
                bookmarks.add(i);

        return bookmarks;
    }

    private void setBook() {
        int index = getIntent().getIntExtra("IndexInRecentBooks", -1);
        startPage = getIntent().getIntExtra("Page", -1);
        book = FileWorker.getInstance().getRecentBooks().get(index);
        totalRead = book.getTotalRead();
    }
}
