package com.example.raw.app;


import com.example.raw.app.Main.Tabs.Tab;
import com.example.raw.app.Main.Tabs.TabLocalBooks;
import com.example.raw.app.Main.Tabs.TabRecentBooks;

final public class TabsKeeper {
    private static final TabsKeeper INSTANCE = new TabsKeeper();
    private TabLocalBooks localBooks;
    private TabRecentBooks recentBooks;

    public static TabsKeeper getInstance() {
        return INSTANCE;
    }

    private TabsKeeper() {}

    public void setTab(Tab tab) {
        if (tab.getClass().equals(TabLocalBooks.class))
            localBooks = (TabLocalBooks) tab;
        else if (tab.getClass().equals(TabRecentBooks.class))
            recentBooks = (TabRecentBooks) tab;
    }

    public void notifyDataSetChanged() {
        recentBooks.dataSetChanged();
        localBooks.dataSetChanged();
    }
}
