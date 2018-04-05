package com.example.raw.app.Main;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.raw.app.R;

import java.io.File;
import java.util.ArrayList;

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


        String path = "/system/fonts";
        File file = new File(path);
        ArrayList<String> items = new ArrayList<>();
        for(File ff : file.listFiles())
            items.add(ff.getName());

        Spinner spinner = findViewById(R.id.acSettingsFontsSpinner);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>
                (this, android.R.layout.simple_spinner_item,
                        items);
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerArrayAdapter);
        /*
        * Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Kokila.ttf");
   txtyour.setTypeface(type);
        * */

        LinearLayout layout = findViewById(R.id.acSettingsMainLayout);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean autoStart = sharedPref.getBoolean(getString(R.string.settings_autoStart), false);

        View view = LayoutInflater.from(this).inflate(R.layout.settings_switch_item, layout, false);
        ((TextView) view.findViewById(R.id.acSettingsItemText)).setText(R.string.settings_autoStart);
        SwitchCompat switchCompat = view.findViewById(R.id.acSettingsItemSwitch);

        switchCompat.setChecked(autoStart);
        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.settings_autoStart), isChecked);
                editor.apply();
            }
        });

        layout.addView(view);
    }
}
