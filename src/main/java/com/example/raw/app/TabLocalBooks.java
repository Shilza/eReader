package com.example.raw.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;


public class TabLocalBooks extends Tab{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_local_books, null);

        RecyclerView recyclerView = view.findViewById(R.id.local_books_recycler_view);
        tvLocation = view.findViewById(R.id.local_books_text);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = FileWorker.getInstance().getLocalBooks();
        adapter = new LocalBooksRVAdapter(books, getActivity(), this);
        recyclerView.setAdapter(adapter);

        locationName = "Локальные";

        if(books.isEmpty())
            tvLocation.setText(locationName);

        return view;
    }
}
