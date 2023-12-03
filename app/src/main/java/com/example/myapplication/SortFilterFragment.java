package com.example.myapplication;
/**
 * This class represents a fragment that allows users to sort and filter items based different criteria
*/
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
import java.util.List;
import java.util.stream.Collectors;


public class SortFilterFragment extends DialogFragment implements TagFragment.TagSelectionListener{
    /**
     * This is an interface for communication between the fragment and its host activity.
     */
    private SortFilterInteractionInterface listener;
    public interface SortFilterInteractionInterface {
        /**
         * This is called when the user presses the "OK" button after selecting their sorting and filtering options.
         *  @param sortOption   The selected sorting option (e.g., date, description, make, value).
         *  @param ascending    True if sorting should be in ascending order, false otherwise.
         *  @param filterOption The selected filter option (not yet implemented in this example).
         *  @param dateFrom      The starting date for filtering (if applicable).
         *  @param dateTo        The ending date for filtering (if applicable).
         */
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
    private String selectedTag;

    private TagFragment.TagSelectionListener tagSelectionListener;

    /**
     * Called when the fragment is attached to an activity.
     *
     * @param context The context to which the fragment is attached.
     */
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof SortFilterInteractionInterface) {
            listener = (SortFilterInteractionInterface) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement SortFilterInteractionInterface");
        }

        if (context instanceof TagFragment.TagSelectionListener) {
            tagSelectionListener = (TagFragment.TagSelectionListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement TagSelectionListener");
        }

    }
    @Override
    public void onTagSelected(String tag) {
        selectedTag = tag;
    }

    @Override
    public void onTagListUpdated(List<String> updatedTagList) {
        // Update the tag list in the search view
        // This can be done if you have a reference to the search view's adapter
    }
    //////////////

  //  @Override
   // public void SortFilterFragmentOKPressed(int sort_option, boolean asc, int filter_option, String date_from, String date_to) {
        // ...

        // Handle filtering by tag if filter_option corresponds to tag filtering
       // if (filter_option == YOUR_TAG_FILTER_OPTION_CONSTANT) {
            // Filter the items based on the selected tag
        //    List<Item> filteredItems = filterByTag(visibleItems, selectedTag);
            // Further handle the filteredItems, update UI, etc.
        //    itemListAdapter.updateFilteredItems(filteredItems);
       // }
   // }

    // Helper method to filter items by tag
    private List<Item> filterByTag(List<Item> itemList, String tag) {
        return itemList.stream()
                .filter(item -> item.getTag().equals(tag))
                .collect(Collectors.toList());
    }

    /**
     * Called to create the dialog displayed by this fragment.
     *
     * @param savedInstanceState If the fragment is being re-created from a previous saved state, this is the state.
     * @return A new Dialog instance.
     */
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
