package com.example.raw.app.Main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.raw.app.R;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.acSettingsActionBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        LinearLayout layout = findViewById(R.id.acSettingsMainLayout);
        for (int i = 0; i < 5; i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.settings_item, layout, false);
            layout.addView(view);
        }
    }
}
