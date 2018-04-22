package com.example.raw.app.Main.Adapters;

import android.view.MenuItem;

public interface Select {
    byte CONTEXT_MENU_OPEN = 0;
    byte CONTEXT_MENU_PROPERTIES = 1;

    default void itemSelectedProcessing(MenuItem item){

        switch (item.getItemId()) {
            case CONTEXT_MENU_OPEN:
                bookOpening();
                break;

            case CONTEXT_MENU_PROPERTIES:
                openProperties();
                break;
        }
    }

    void bookOpening();
    void openProperties();
}
