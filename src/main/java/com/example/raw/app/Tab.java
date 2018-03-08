package com.example.raw.app;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public abstract class Tab extends Fragment {

    RVAdapter adapter;
    TextView tvLocation;
    String locationName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TabKeeper.setTab(this);
    }

    @Override
    public abstract View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState);

    void dataSetChanged(){
        if(adapter.getItemCount() == 0)
            tvLocation.setText(locationName);
        else
            tvLocation.setText("");

        adapter.notifyDataSetChanged();
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
