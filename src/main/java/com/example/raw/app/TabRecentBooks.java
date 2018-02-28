package com.example.raw.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class TabRecentBooks extends Fragment {

    private RecyclerView recyclerView;
    private static RVAdapter adapter;
    private static TextView tvRecentBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_recent_books, null);

        recyclerView = view.findViewById(R.id.recent_books_recycler_view);
        tvRecentBooks = view.findViewById(R.id.recent_books_text);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = FileWorker.JSONWorker.getBooks();
        adapter = new RVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);

        if(books.isEmpty())
           tvRecentBooks.setText("Последние");

        return view;
    }

    static void addBook(){
        adapter.notifyItemInserted(adapter.getItemCount());
        tvRecentBooks.setText("");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}