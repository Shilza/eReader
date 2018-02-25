package com.example.raw.app;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ContextMenuProperties extends AppCompatActivity {

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
        ivCover.setImageResource(book.coverId);
        tvName.setText(book.name);
        tvSize.setText(book.size);
        tvTotalRead.setText("0%");
        tvLastActivity.setText("0:00:00");
        tvFilePath.setText(book.filePath);
    }
}
