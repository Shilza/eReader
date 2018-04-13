package com.example.raw.app.Viewers;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.example.raw.app.R;
import com.folioreader.model.HighLight;
import com.folioreader.ui.base.OnSaveHighlight;
import com.folioreader.util.FolioReader;
import com.folioreader.util.OnHighlightListener;

import java.util.ArrayList;
import java.util.Date;

public class EPUBViewer extends AppCompatActivity implements OnHighlightListener {

    private FolioReader folioReader;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_epubviewer);
        folioReader = new FolioReader(this);
        folioReader.registerHighlightListener(this);
        final String path = getIntent().getStringExtra("Filepath");
        findViewById(R.id.btn_assest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folioReader.openBook("file:///"+path);
            }
        });
        getHighlightsAndSave();
    }

    private void getHighlightsAndSave() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<HighLight> highlightList  = new ArrayList<>();
                highlightList.add(new HighlightData("1620217580", "Sir Arthur", new Date(23542342),
                        null, 0,
                        "0", "12$22$1$highlight_yellow$", "yellow", "32f24e14-7199-437e-a526-46dd607fe311"));

                if (highlightList == null) {
                    folioReader.saveReceivedHighLights(highlightList, new OnSaveHighlight() {
                        @Override
                        public void onFinished() {
                            //You can do anything on successful saving highlight list
                        }
                    });
                }
            }
        }).start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        folioReader.unregisterHighlightListener();
    }

    @Override
    public void onHighlight(HighLight highlight, HighLight.HighLightAction type) {
        Toast.makeText(this,
                "highlight id = " + highlight.getUUID() + " type = " + type,
                Toast.LENGTH_SHORT).show();
    }
}