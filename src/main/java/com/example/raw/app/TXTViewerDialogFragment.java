package com.example.raw.app;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;

public class TXTViewerDialogFragment extends DialogFragment{
    private TextView textView;
    private String comingString;
    private Context context;
    private String[] array;

    public TXTViewerDialogFragment(){}

    void set(TextView textView, String comingString, String[] array, Context context){
        this.textView = textView;
        this.comingString = comingString;
        this.context = context;
        this.array = array;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Выберите кодировку")
                .setSingleChoiceItems(array, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {

                            }
                        })
                .setPositiveButton("Oк", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            textView.setText(new String(comingString.getBytes(), "windows-1251"));
                        }catch (UnsupportedEncodingException ex){}
                    }
                })
                .setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    }
                });

        return builder.create();
    }
}
