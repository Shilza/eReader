package com.example.raw.app.GUI;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class RVViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    public RVViewHolder(View itemView){
        super(itemView);
        itemView.setOnClickListener(this);
    }
}