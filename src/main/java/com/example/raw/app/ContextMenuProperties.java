package com.example.raw.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;


public class ContextMenuProperties extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_menu_properties);

        Book book = (Book)getIntent().getSerializableExtra("Book");

        ((ImageView) findViewById(R.id.properties_book_cover)).setImageResource(R.drawable.e);
        ((TextView) findViewById(R.id.properties_book_name)).setText(book.getName());
        ((TextView) findViewById(R.id.properties_total_read)).setText(book.getTotalRead()*100 + "%");
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
