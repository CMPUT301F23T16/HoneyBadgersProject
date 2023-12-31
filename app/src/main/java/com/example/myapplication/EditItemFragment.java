package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Class encapsulates business logic for the AddItemFragment
 */
public class EditItemFragment extends DialogFragment {
    private EditText itemName;
    private TextView purchaseDate;
    private EditText itemDescription;
    private EditText itemMake;
    private EditText itemModel;
    private EditText itemSerial;
    private EditText itemPrice;
    private EditText itemComment;
    public TextView itemTag;
    private Button scan_button;
    private ItemInfoFetcher infoFetcher;

    private DatePickerDialog datePickerDialog;
    private EditItemInteractionInterface listener;
    private TagFragmentListener tagFragmentListener;

    /**
     * This method is attaches the Activity to an attribute of the fragment
     * @param context this is the context of the activity that the fragment will be attached to
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ((ItemListActivity) context).editItemFragment = this;
        if (context instanceof EditItemInteractionInterface) {
            listener = (EditItemInteractionInterface) context;
        }
        else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
        if (context instanceof TagFragmentListener) {
            tagFragmentListener = (TagFragmentListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "TagFragmentListener is not implemented");
        }
    }

    /**
     * Interface used to attach an activity to this Fragment
     * Enables methods defined in activity to be used in the fragment
     */
    public interface EditItemInteractionInterface {
        void EditFragmentOKPressed(Item item);
        List<String> getPhotoReferences();
        void saveTemporaryState(Item item);
    }

    /**
     * Method creates and launches an EditItemFragment
     *      Also saves an Item object in the Fragment's bundle
     * @param item Object to be saved in the Fragment's bundle
     * @return EditItemFragment i.e., the fragment that will pop up
     */

    public static EditItemFragment newInstance(Item item){
        Bundle args = new Bundle();
        args.putSerializable("item", item);

        EditItemFragment fragment = new EditItemFragment();
        fragment.setArguments(args);

        return fragment;
    }

    /**
     * Function encapsulates the logic behind all components in the EditItemFragment that will show up
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
        itemTag = view.findViewById(R.id.add_item_tag);
        scan_button = view.findViewById(R.id.scan_button);

        // Get clicked on item from bundle and set view values
        Item clickedItem = (Item) getArguments().getSerializable("item");
        itemName.setText(clickedItem.getName());
        purchaseDate.setText(clickedItem.getDateAdded());
        itemDescription.setText(clickedItem.getDescription());
        itemMake.setText(clickedItem.getMake());
        itemModel.setText(clickedItem.getModel());
        itemSerial.setText(clickedItem.getSerial());
        itemPrice.setText(clickedItem.getPrice().toString());
        itemComment.setText(clickedItem.getComment());
        itemTag.setText(TextUtils.join(",",clickedItem.getTag()));

        itemTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTagFragment();
            }
        });


        // BUTTON CLICK TRIGGERS ZXING BARCODE SCANNER
        // SUCCESSFUL SCAN TRIGGERS onActivityResult
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(EditItemFragment.this);
                integrator.setOrientationLocked(true);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan a barcode or QR code");
                integrator.initiateScan();
            }
        });

        // Coding logic for date picker
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
                .setTitle("Edit item")
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
                        saveState();
                        new PhotosFragment().show(getActivity().getSupportFragmentManager(), "photos");
                    }
                });;
        AlertDialog dialogue = builder.create();
        dialogue.show();

        /*
            - Implemented Error handling functionality
            - Eg Dialogue will not be dismissed if user leaved mandatory fields empty
         */
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
                List<String> tag = Arrays.asList(itemTag.getText().toString().split(","));

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
                        listener.EditFragmentOKPressed(temp);
                        dialogue.dismiss();
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

        return dialogue;
    }
    private void openTagFragment(){
        tagFragmentListener.onOpenTagFragment();
    }




    private void saveState() {
        String name = itemName.getText().toString();
        String price = itemPrice.getText().toString();
        String dateAdded = purchaseDate.getText().toString();
        String description = itemDescription.getText().toString();
        String make = itemMake.getText().toString();
        String model = itemModel.getText().toString();
        String serial = itemSerial.getText().toString();
        String comment = itemComment.getText().toString();
        List<String> tag = Arrays.asList(itemTag.getText().toString().split(","));
        try {
            if (dateAdded.trim().length() == 0)
                listener.saveTemporaryState(new Item(name, price.trim().length() == 0 ? -1 : Double.parseDouble(price), "",
                        description, make, model, serial, comment, tag, null));
            else
                listener.saveTemporaryState(new Item(name, price.trim().length() == 0 ? -1 : Double.parseDouble(price), new SimpleDateFormat("yyyy-MM-dd").parse(dateAdded),
                        description, make, model, serial, comment, tag, null));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Method triggered when barcode scan or gallery image picker succeeds
     * The result from those intents can be handled appropriate here
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        // UPDATE PRODUCT SERIAL AND DESCRIPTION FROM BARCODE
        // BARCODE OBTAINED FROM BARCODE SCAN FROM CAMERA

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                String barCode = result.getContents();
                Toast.makeText(requireContext(), "Scanned: " + barCode, Toast.LENGTH_LONG).show();

                // Process the scanned barcode or QR code here
                itemSerial.setText(result.getContents());

                // Get description from barcode
                infoFetcher = new ItemInfoFetcher(new NetworkHandler(), requireContext());
                infoFetcher.fetchProductInfo(barCode, new ItemInfoFetcher.ProductInfoCallback() {
                    @Override
                    public void onSuccess(String description) {
                        itemDescription.setText(description);
                    }
                    @Override
                    public void onError(String error) {
                        itemDescription.setText(error);
                    }
                });
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);

        }
    }
}
