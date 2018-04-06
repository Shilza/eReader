package com.example.raw.app.Main.Navigation;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.raw.app.R;
import com.example.raw.app.Utils.BookOpener;

import java.io.File;

public class NavigationView {

    private Context context;
    private ActionBarDrawerToggle toggle;

    public NavigationView(final Context context, ActionBarDrawerToggle toggle) {
        this.context = context;
        this.toggle = toggle;

        View view = LayoutInflater.from(context).inflate(R.layout.activity_main, null);

        DrawerLayout drawer = view.findViewById(R.id.acMainDrawerLayout);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    public ActionBarDrawerToggle getToggle(){
        return toggle;
    }

    public void bookOpening(Intent data) {
        Uri selectedFile = data.getData();
        File file = new File(getRealPathFromURI(selectedFile));

        try {
            BookOpener.getInstance().opening(file, context);
        } catch (IllegalArgumentException e) {
            Toast.makeText(context, R.string.error_unsupported_format, Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = context.getContentResolver().query(
                contentURI, null, null, null, null);

        if (cursor == null)
            result = contentURI.getPath();
        else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(index);
            cursor.close();
        }

        return result;
    }

}
