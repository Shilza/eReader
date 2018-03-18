package com.example.raw.app;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;


public class Dialog extends DialogFragment {

    public interface OnInputListener{
        void sendInput(int value);
    }

    public OnInputListener onInputListener;
    private EditText input;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog, container, false);
        TextView mActionOk = view.findViewById(R.id.pdf_view_dialog_goto_button);
        getDialog().setTitle("Перейти к");
        input = view.findViewById(R.id.pdf_view_dialog_goto_input);

        SeekBar seekBar = view.findViewById(R.id.pdf_view_dialog_goto_seekBar);
        seekBar.setMax(getArguments().getInt("maxSeekBarValue"));
        seekBar.setProgress(getArguments().getInt("currentSeekBarValue"));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                input.setText(String.valueOf(i));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int value = Integer.parseInt(input.getText().toString());
                onInputListener.sendInput(value);
                getDialog().dismiss();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
            onInputListener = (OnInputListener) getActivity();
    }
}
