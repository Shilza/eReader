package com.example.raw.app;


final public class TabKeeper {
    private static final TabKeeper INSTANCE = new TabKeeper();
    private TabLocalBooks localBooks;
    private TabRecentBooks recentBooks;

    public static TabKeeper getInstance() {
        return INSTANCE;
    }

    private TabKeeper(){}

    void setTab(Tab tab){
        if(tab.getClass().equals(TabLocalBooks.class))
            localBooks = (TabLocalBooks)tab;
        else if(tab.getClass().equals(TabRecentBooks.class))
            recentBooks = (TabRecentBooks)tab;
    }

    public void notifyDataSetChanged(){
        recentBooks.dataSetChanged();
        localBooks.dataSetChanged();
    }
}
