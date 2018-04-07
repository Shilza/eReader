package com.example.raw.app.Main.Navigation;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.raw.app.Main.Navigation.Adapters.FontsSpinnerAdapter;
import com.example.raw.app.R;

import java.util.ArrayList;

public class SettingsActivity extends AppCompatActivity {

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

        initSpinner();

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

    private void initSpinner(){
        ArrayList<String> items = new ArrayList<>();
        items.add("serif");
        items.add("cursive");
        items.add("monospace");
        items.add("sans-serif");
        items.add("sans-serif-black");
        items.add("sans-serif-condensed");
        items.add("sans-serif-condensed-light");
        items.add("sans-serif-light");
        items.add("sans-serif-medium");
        items.add("sans-serif-smallcaps");
        items.add("sans-serif-thin");
        items.add("serif-monospace");

        Spinner spinner = findViewById(R.id.acSettingsFontsSpinner);

        FontsSpinnerAdapter adapter = new FontsSpinnerAdapter(
                this,
                android.R.layout.simple_spinner_item,
                items
        );
        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String defaultFont = sharedPref.getString(getString(R.string.settings_fonts), items.get(0));

        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(defaultFont));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Object item = parent.getItemAtPosition(pos);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.settings_fonts), item.toString());
                editor.apply();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

}

