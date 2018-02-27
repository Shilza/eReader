package com.example.raw.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

public class ContextMenuProperties extends AppCompatActivity{

    private ImageView ivCover;
    private TextView tvName;
    private TextView tvTotalRead;
    private TextView tvLastActivity;
    private TextView tvSize;
    private TextView tvFilePath;
    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_context_menu_properties);

        ivCover = findViewById(R.id.properties_book_cover);
        tvName = findViewById(R.id.properties_book_name);
        tvTotalRead = findViewById(R.id.properties_total_read);
        tvLastActivity = findViewById(R.id.properties_last_activity);
        tvSize = findViewById(R.id.properties_book_size);
        tvFilePath = findViewById(R.id.properties_file_path);

        book = (Book)getIntent().getSerializableExtra("Book");
        ivCover.setImageResource(book.getCoverId());
        tvName.setText(book.getName());
        tvSize.setText(book.getSize());
        tvTotalRead.setText(book.getTotalRead());
        tvLastActivity.setText(book.getLastActivity());
        tvFilePath.setText(book.getFilePath());

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
