package com.example.raw.app.Main;


import com.example.raw.app.Main.Tabs.Tab;
import com.example.raw.app.Main.Tabs.TabLocalBooks;
import com.example.raw.app.Main.Tabs.TabRecentBooks;

public class RVMediator {
    private static final RVMediator INSTANCE = new RVMediator();

    private TabLocalBooks tabLocalBooks;
    private TabRecentBooks tabRecentBooks;

    public static RVMediator getInstance() {
        return INSTANCE;
    }

    private RVMediator() {
    }

    public void setTab(Tab tab) {
        if (tab.getClass().equals(TabLocalBooks.class))
            tabLocalBooks = (TabLocalBooks) tab;
        else if (tab.getClass().equals(TabRecentBooks.class))
            tabRecentBooks = (TabRecentBooks) tab;
    }


    public void notifyDataSetChanged(){
        tabLocalBooks.notifyDataSetChanged();
        tabRecentBooks.notifyDataSetChanged();
    }

    public void notifyItemRemoved(int position){
        tabRecentBooks.notifyItemRemoved(position);
    }
}
