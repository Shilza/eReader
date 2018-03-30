package com.example.raw.app.Viewers.Dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.raw.app.R;

public class GoToDialog extends DialogFragment {

    public interface OnInputListener {
        void sendInput(int value);
    }

    public OnInputListener onInputListener;
    private int pageCount;
    private EditText input;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_goto_pdf_viewer, container, false);
        Button actionOk = view.findViewById(R.id.acPDFViewerDialogGotoActionGo);
        getDialog().setTitle("Перейти к");

        onInputListener = (OnInputListener) getActivity();

        input = view.findViewById(R.id.acPDFViewerDialogGotoEditText);

        SeekBar seekBar = view.findViewById(R.id.acPDFViewerDialogGotoSeekBar);
        pageCount = getArguments().getInt("maxPageCount");
        seekBar.setMax(pageCount);
        seekBar.setProgress(getArguments().getInt("currentPage") + 1);
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

        actionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (input.getText().length() != 0) {
                    int value = Integer.parseInt(input.getText().toString()) - 1;

                    if (value <= pageCount) {
                        onInputListener.sendInput(value);
                        dismiss();
                    } else
                        Toast.makeText(getActivity(), "Такой страницы не существует", Toast.LENGTH_SHORT).show();
                } else {
                    dismiss();
                    Toast.makeText(getActivity(), "Вы не ввели страницу", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
