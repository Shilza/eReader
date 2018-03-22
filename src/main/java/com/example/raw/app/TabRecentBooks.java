package com.example.raw.app;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Entities.Book;

import java.util.ArrayList;

public class TabRecentBooks extends Tab {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_recent_books, null);

        RecyclerView recyclerView = view.findViewById(R.id.recent_books_recycler_view);
        tvLocation = view.findViewById(R.id.recent_books_text);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = FileWorker.getInstance().getRecentBooks();
        adapter = new RecentBooksRVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);
        locationName = "Последние";

        if(books.isEmpty())
           tvLocation.setText(locationName);

        return view;
    }
}