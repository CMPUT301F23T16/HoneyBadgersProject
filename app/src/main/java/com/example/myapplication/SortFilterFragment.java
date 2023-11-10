package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class SortFilterFragment extends DialogFragment {
    private SortFilterInteractionInterface listener;
    public interface SortFilterInteractionInterface {
        void SortFilterFragmentOKPressed(int sort_option,boolean asc,int filter_option, String date_from, String date_to);
    }
    private RadioGroup sorting_criteria_rg;
    private DatePickerDialog datePickerDialogFrom;
    private TextView date_from;
    private DatePickerDialog datePickerDialogTo;
    private TextView date_to;
    private EditText description;
    private EditText make;
    private ToggleButton ascending;
    private int filtering_option;
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SortFilterInteractionInterface) {
            listener = (SortFilterInteractionInterface) context;
        }
        else {
            throw new RuntimeException(context.toString() + "SortFilterInteractionInterface is not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        filtering_option=-1;
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_filter_fragment_layout, null);
        sorting_criteria_rg = view.findViewById(R.id.sort_button_group);
        date_from = view.findViewById(R.id.date_from);
        date_to = view.findViewById(R.id.date_to);
        if(!(date_from.getText().toString().equals("")||date_from.getText().toString().equals("")))
            filtering_option=R.id.date_from;
        description = view.findViewById(R.id.description_keyword);
        make =view.findViewById(R.id.make_keyword);
        ascending = view.findViewById(R.id.sortToggleButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        builder.setView(view)
                .setTitle("Sort/filter item")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICK", "onClick: "+sorting_criteria_rg.getCheckedRadioButtonId());
                        listener.SortFilterFragmentOKPressed(
                                sorting_criteria_rg.getCheckedRadioButtonId(),
                                ascending.getText().toString().toUpperCase().equals("ASCENDING")?true:false,filtering_option,
                                date_from.getText().toString(),
                                date_to.getText().toString()
                                );
                    }
                });
       return builder.create();

    }
}
