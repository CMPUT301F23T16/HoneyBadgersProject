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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.List;

/**
 * Class encapsulates business logic for the AddItemFragment
 */
public class AddItemFragment extends DialogFragment {
    private EditText itemName;
    private TextView purchaseDate;
    private EditText itemDescription;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemSerial;
    private EditText itemPrice;
    private EditText itemComment;
    private EditText itemTag;

    private DatePickerDialog datePickerDialog;
    private AddItemInteractionInterface listener;

    /**
     * This method is attaches the Activity to an attribute of the fragment
     * @param context this is the context of the activity that the fragment will be attached to
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemInteractionInterface) {
            listener = (AddItemInteractionInterface) context;
        }
        else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }

    /**
     * Interface used to attach an activity to this Fragment
     * Enables methods defined in activity to be used in the fragment
     */
    public interface AddItemInteractionInterface {
        void AddFragmentOKPressed(Item item);
        List<String> getPhotoReferences();
        void saveTemporaryState(Item item);
        Item getTemporaryState();
    }

    /**
     * Function encapsulates the logic behind all components in the AddItemFrament that will show up
     *      as a dialogue. It is executed when the Fragment pops up
     * @param savedInstanceState If the fragment is being re-initialized after
     *      previously being shut down then this Bundle contains the data it most
     *      recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.add_edit_item_fragment, null);

        // Bind layout components to java variables
        itemName = view.findViewById(R.id.add_item_name);
        purchaseDate = view.findViewById(R.id.add_item_date);
        itemDescription = view.findViewById(R.id.add_item_description);
        itemMake = view.findViewById(R.id.add_item_make);
        itemModel = view.findViewById(R.id.add_item_model);
        itemSerial = view.findViewById(R.id.add_item_serial_number);
        itemPrice = view.findViewById(R.id.add_item_price);
        itemComment = view.findViewById(R.id.add_item_comment);
        itemTag = view.findViewById(R.id.add_item_tag_spinner);

        if(listener.getTemporaryState()!=null)
            reloadState();
        purchaseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Implementing date picker dialogue
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(
                        requireContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDayOfMonth) {
                                // Handle the selected date here
                                String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDayOfMonth;
                                // You can update a TextView or store the selected date as needed.
                                purchaseDate.setText(selectedDate);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
                datePickerDialog.show();

            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Add/edit item")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Photos", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d("Running SaveState", "onClick: ");
                        saveState();
                        Log.d("Running PhotosFragment", "onClick: ");
                        new PhotosFragment().show(getActivity().getSupportFragmentManager(), "Photos");
                    }
                });
        AlertDialog dialogue = builder.create();
        dialogue.show();

        dialogue.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String dateAdded = purchaseDate.getText().toString();
                String description = itemDescription.getText().toString();
                String make = itemMake.getText().toString();
                String model = itemModel.getText().toString();
                String serial = itemSerial.getText().toString();
                String comment = itemComment.getText().toString();
                String tag = itemTag.getText().toString();

                if (name.trim().length() == 0){
                    Toast.makeText(requireContext(), "Please input an item name!", Toast.LENGTH_SHORT).show();
                }
                else if (price.trim().length() == 0){
                    Toast.makeText(requireContext(), "Please input the item price!", Toast.LENGTH_SHORT).show();
                }
                else if(dateAdded.trim().length() == 0){
                    Toast.makeText(requireContext(), "Please input a purchase date!", Toast.LENGTH_SHORT).show();
                }
                else if(description.trim().length() == 0){
                    Toast.makeText(requireContext(), "Please input an item description!", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        Item temp = new Item(name, Double.parseDouble(price), new SimpleDateFormat("yyyy-MM-dd").parse(dateAdded),
                                description, make, model, serial, comment, tag, listener.getPhotoReferences());
                        listener.AddFragmentOKPressed(temp);
                        dialogue.dismiss();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return dialogue;
    }
    private void saveState(){
        String name = itemName.getText().toString();
        String price = itemPrice.getText().toString();
        String dateAdded = purchaseDate.getText().toString();
        String description = itemDescription.getText().toString();
        String make = itemMake.getText().toString();
        String model = itemModel.getText().toString();
        String serial = itemSerial.getText().toString();
        String comment = itemComment.getText().toString();
        String tag = itemTag.getText().toString();
        try{
            if(dateAdded.trim().length() == 0)
                listener.saveTemporaryState(new Item(name, price.trim().length() == 0?-1:Double.parseDouble(price), "",
                        description, make, model, serial, comment, tag, null));
            else
                listener.saveTemporaryState(new Item(name, price.trim().length() == 0?-1:Double.parseDouble(price), new SimpleDateFormat("yyyy-MM-dd").parse(dateAdded),
                        description, make, model, serial, comment, tag, null));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }
    private void reloadState()
    {
        Item temp = listener.getTemporaryState();
        itemName.setText(temp.getName());
        purchaseDate.setText(temp.getDateAdded());
        itemDescription.setText(temp.getDescription());
        itemMake.setText(temp.getMake());
        itemModel.setText(temp.getModel());
        itemSerial.setText(temp.getSerial());
        if(temp.getPrice()!=-1)
            itemPrice.setText(temp.getPrice().toString());
        itemComment.setText(temp.getComment());
        itemTag.setText(temp.getTag());
    }

}
