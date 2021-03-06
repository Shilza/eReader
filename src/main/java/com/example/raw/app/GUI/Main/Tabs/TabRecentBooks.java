package com.example.raw.app.GUI.Main.Tabs;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.raw.app.Entities.Book;
import com.example.raw.app.GUI.Main.Adapters.RecentBooksRVAdapter;
import com.example.raw.app.R;
import com.example.raw.app.Utils.Repository;

import java.util.ArrayList;

public class TabRecentBooks extends Tab {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_recent_books, null);

        RecyclerView recyclerView = view.findViewById(R.id.acMainTabRecentBooksRecyclerView);
        tvLocation = view.findViewById(R.id.acMainTabRecentBooksLocation);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = Repository.getInstance().getRecentBooks();
        adapter = new RecentBooksRVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);
        locationName = getString(R.string.tab_recent_books_location);

        if(books.isEmpty())
           tvLocation.setText(locationName);

        return view;
    }
}