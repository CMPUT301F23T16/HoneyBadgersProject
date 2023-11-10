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
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.sort_filter_fragment_layout, null);
        sorting_criteria_rg = view.findViewById(R.id.sort_button_group);
        date_from = view.findViewById(R.id.date_from);
        date_to = view.findViewById(R.id.date_to);
        description = view.findViewById(R.id.description_keyword);
        make =view.findViewById(R.id.make_keyword);
        ascending = view.findViewById(R.id.sortToggleButton);
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        date_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementing date picker dialogue
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialogFrom = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                // Handle the selected date here
                                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                                // You can update a TextView or store the selected date as needed.
                                date_from.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialogFrom.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialogFrom.show();

            }
        });

        date_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementing date picker dialogue
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialogTo = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                // Handle the selected date here
                                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                                // You can update a TextView or store the selected date as needed.
                                date_to.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialogTo.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialogTo.show();

            }
        });



        builder.setView(view)
                .setTitle("Sort/filter item")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICK", "onClick: "+sorting_criteria_rg.getCheckedRadioButtonId());
                        listener.SortFilterFragmentOKPressed(
                                sorting_criteria_rg.getCheckedRadioButtonId(),
                                ascending.getText().toString().toUpperCase().equals("ASCENDING")?true:false,0,
                                date_from.getText().toString(),
                                date_to.getText().toString()
                                );
                    }
                });
       return builder.create();

    }
}
