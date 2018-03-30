package com.example.raw.app.Main;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.raw.app.Entities.Book;
import com.example.raw.app.R;
import com.example.raw.app.Utils.FileWorker;

import java.text.DecimalFormat;


public class PropertiesActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_properties);

        Book book = (Book)getIntent().getSerializableExtra("Book");
        String totalRead;
        if(book.getTotalRead() > 0){
            DecimalFormat f = new DecimalFormat("##.0");
            totalRead = f.format(book.getTotalRead()*100) + "%";
        } else
            totalRead = "0%";

        ImageView cover =  findViewById(R.id.acPropertiesBookCover);
        Glide.with(this)
                .load(FileWorker.getInstance().getPicturesPath() + book.getName()+".png")
                .apply(new RequestOptions().fitCenter().placeholder(R.drawable.e))
                .into(cover);

        ((TextView) findViewById(R.id.acPropertiesBookName)).setText(book.getName());
        ((TextView) findViewById(R.id.acPropertiesTotalRead)).setText(totalRead);
        ((TextView) findViewById(R.id.acPropertiesLastActivity)).setText(book.getLastActivity());
        ((TextView) findViewById(R.id.acPropertiesBookSize)).setText(book.getSize());
        ((TextView) findViewById(R.id.acPropertiesFilePath)).setText(book.getFilePath());

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
