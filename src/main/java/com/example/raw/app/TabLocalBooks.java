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


public class TabLocalBooks extends Fragment {

    private RecyclerView recyclerView;
    private RVAdapter adapter;
    private static TextView tvLocalBooks;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.tab_local_books, null);

        recyclerView = view.findViewById(R.id.local_books_recycler_view);
        tvLocalBooks = view.findViewById(R.id.local_books_text);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);

        ArrayList<Book> books = FileWorker.getLocalBooks();
        adapter = new LocalBooksRVAdapter(books, getActivity());
        recyclerView.setAdapter(adapter);

        if(books.isEmpty())
            tvLocalBooks.setText("Локальные");

        return view;
    }

    static void setTextLocalBooks(){
        tvLocalBooks.setText("Локальные");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public boolean onContextItemSelected(MenuItem item){
        adapter.getItemSelected(item);
        return  super.onContextItemSelected(item);
    }

}
