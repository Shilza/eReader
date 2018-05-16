package com.example.raw.app.GUI.Main.Navigation;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.raw.app.GUI.Main.Navigation.Adapters.FontsSpinnerAdapter;
import com.example.raw.app.R;
import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;

import java.util.ArrayList;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.acSettingsActionBack).setOnClickListener((View v) -> {
            finish();
        });

        initFontsSpinner();
        initFontSizeSpinner();
        colorPickerInit();

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        boolean autoStart = sharedPref.getBoolean(getString(R.string.settings_autoStart), false);

        ((TextView) findViewById(R.id.acSettingsItemText)).setText(R.string.settings_autoStart);
        SwitchCompat switchCompat = findViewById(R.id.acSettingsItemSwitch);

        switchCompat.setChecked(autoStart);
        switchCompat.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.settings_autoStart), isChecked);
            editor.apply();
        });
    }

    private void initFontSizeSpinner() {
        Float[] data = {6f, 8f, 10f, 12f, 14f, 16f, 18f, 20f, 22f, 24f, 26f, 28f, 36f, 48f, 72f};

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Float defaultFontSize = sharedPref.getFloat(getString(R.string.settings_font_size), data[3]);

        ArrayAdapter<Float> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, data);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = findViewById(R.id.acSettingsFontSizeSpinner);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(defaultFontSize));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putFloat(getString(R.string.settings_font_size), (float) item);
                editor.apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initFontsSpinner() {
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
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);

                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.settings_fonts), item.toString());
                editor.apply();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void colorPickerInit() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        //defValue color black
        int fontColor = sharedPref.getInt(getString(R.string.settings_font_color), -16777216);
        Button colorPickerButton = findViewById(R.id.acSettingsColorButton);
        colorPickerButton.setBackgroundColor(fontColor);

        ColorPicker cp = new ColorPicker(this);
        cp.setCallback((@ColorInt int color) -> {
            colorPickerButton.setBackgroundColor(color);

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putInt(getString(R.string.settings_font_color), color);
            editor.apply();

            cp.dismiss();
        });

        colorPickerButton.setOnClickListener((View v) -> {
            cp.show();
        });
    }
}

