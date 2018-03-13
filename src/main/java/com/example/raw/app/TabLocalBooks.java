package com.example.raw.app;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

public class TabLocalBooks extends Tab implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_local_books, null);


        swipeRefreshLayout = view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);

        RecyclerView recyclerView = view.findViewById(R.id.local_books_recycler_view);
        tvLocation = view.findViewById(R.id.local_books_text);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = FileWorker.getInstance().getLocalBooks();
        adapter = new LocalBooksRVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);

        locationName = "Локальные";

        if(books.isEmpty())
            tvLocation.setText(locationName);

        return view;
    }

    class Sas extends Thread{
        public void run(){
            FileWorker.getInstance().localBooksSearching();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    dataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
    }

    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        new Sas().start();
    }
}
