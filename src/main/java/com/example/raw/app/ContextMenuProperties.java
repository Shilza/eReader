package com.example.raw.app;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.Utils.FileWorker;

import java.io.File;
import java.text.DecimalFormat;


public class ContextMenuProperties extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_menu_properties);

        Book book = (Book)getIntent().getSerializableExtra("Book");
        String totalRead;
        if(book.getTotalRead() > 0){
            DecimalFormat f = new DecimalFormat("##.0");
            totalRead = f.format(book.getTotalRead()*100) + "%";
        } else
            totalRead = "0%";

        ImageView cover =  findViewById(R.id.properties_book_cover);
        Glide.with(this)
                .load(FileWorker.getInstance().getPicturesPath() + book.getName()+".png")
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(cover);

        ((TextView) findViewById(R.id.properties_book_name)).setText(book.getName());
        ((TextView) findViewById(R.id.properties_total_read)).setText(totalRead);
        ((TextView) findViewById(R.id.properties_last_activity)).setText(book.getLastActivity());
        ((TextView) findViewById(R.id.properties_book_size)).setText(book.getSize());
        ((TextView) findViewById(R.id.properties_file_path)).setText(book.getFilePath());

        /*
        tvFilePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE, Uri.parse(Environment.getExternalStorageDirectory ()+"/eReader"));
                startActivityForResult(myIntent, 2);
            }
        });
        */
    }
}
