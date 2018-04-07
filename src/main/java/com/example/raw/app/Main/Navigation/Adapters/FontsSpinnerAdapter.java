package com.example.raw.app.Main.Navigation.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class FontsSpinnerAdapter extends ArrayAdapter<String> {

    private ArrayList<Typeface> fonts;

    public FontsSpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        fonts = new ArrayList<>();
        for(String font : items)
            fonts.add(Typeface.create(font, Typeface.NORMAL));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(fonts.get(position));
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(fonts.get(position));
        return view;
    }
}