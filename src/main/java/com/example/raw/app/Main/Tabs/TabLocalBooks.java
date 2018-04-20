package com.example.raw.app.Main.Tabs;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.Main.Adapters.LocalBooksRVAdapter;
import com.example.raw.app.R;
import com.example.raw.app.Utils.BookSearcher;
import com.example.raw.app.Utils.Repository;

import java.util.ArrayList;

public class TabLocalBooks extends Tab implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_local_books, null);

        swipeRefreshLayout = view.findViewById(R.id.acMainTabLocalBooksSwipeLayout);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.acMainTabLocalBooksRecyclerView);
        tvLocation = view.findViewById(R.id.acMainTabLocalBooksLocation);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = Repository.getInstance().getLocalBooks();
        adapter = new LocalBooksRVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);

        locationName = getString(R.string.tab_local_books_location);

        if(books.isEmpty())
            tvLocation.setText(locationName);

        return view;
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Repository.getInstance().addUniqueLocalBooks(BookSearcher.getInstance().localBooksSearching());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dataSetChanged();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }).start();
    }
}
