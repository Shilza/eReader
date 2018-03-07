package com.example.raw.app;


final class TabKeeper {
    private static TabLocalBooks localBooks;
    private static TabRecentBooks recentBooks;

    
    static void setTab(Tab tab){
        if(tab.getClass().equals(TabLocalBooks.class))
            localBooks = (TabLocalBooks)tab;
        else if(tab.getClass().equals(TabRecentBooks.class))
            recentBooks = (TabRecentBooks)tab;
    }

    static void notifyDataSetChanging(){
        recentBooks.dataSetChanging();
        localBooks.dataSetChanging();
    }
}
