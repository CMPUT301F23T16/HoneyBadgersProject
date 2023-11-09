package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

public class SortFilterFragment extends DialogFragment {
    private SortFilterInteractionInterface listener;
    public interface SortFilterInteractionInterface {
        void SortFilterFragmentOKPressed(int sort_option,boolean asc,int filter_option);
    }
    private RadioGroup sorting_criteria_rg;
    private EditText date_from;
    private EditText date_to;
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
        builder.setView(view)
                .setTitle("Add/edit item")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("ONCLICK", "onClick: "+sorting_criteria_rg.getCheckedRadioButtonId());
                        listener.SortFilterFragmentOKPressed(sorting_criteria_rg.getCheckedRadioButtonId(),ascending.getText().toString().toUpperCase().equals("ASCENDING")?true:false,0);
                    }
                });
       return builder.create();

    }
}
