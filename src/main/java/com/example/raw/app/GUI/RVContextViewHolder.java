package com.example.raw.app.GUI;

import android.view.View;

public abstract class RVContextViewHolder extends RVViewHolder
        implements View.OnCreateContextMenuListener, View.OnLongClickListener{

    private ItemClickListener itemClickListener;

    public RVContextViewHolder(View itemView){
        super(itemView);

        itemView.setOnCreateContextMenuListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public void onClick(View view){
        this.itemClickListener.onItemViewClick(getLayoutPosition(), false);
    }

    @Override
    public boolean onLongClick(View view) {
        this.itemClickListener.onItemViewClick(getLayoutPosition(), true);
        return false;
    }

    public void setOnLongClickListener(ItemClickListener listener) {
        this.itemClickListener = listener;
    }
}
