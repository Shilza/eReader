package com.example.raw.app;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class GoToDialog extends DialogFragment {

    public interface OnInputListener{
        void sendInput(int value);
    }

    public OnInputListener onInputListener;
    private int pageCount;
    private EditText input;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pdf_viewer_go_to_dialog, container, false);
        TextView mActionOk = view.findViewById(R.id.pdf_view_dialog_goto_button);
        getDialog().setTitle("Перейти к");

        onInputListener = (OnInputListener) getActivity();

        input = view.findViewById(R.id.pdf_view_dialog_goto_input);

        SeekBar seekBar = view.findViewById(R.id.pdf_view_dialog_goto_seekBar);
        pageCount = getArguments().getInt("maxSeekBarValue");
        seekBar.setMax(pageCount);
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
                if(value <= pageCount){
                    onInputListener.sendInput(value);
                    getDialog().dismiss();
                } else
                    Toast.makeText(getActivity() , "Такой страницы не существует", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
