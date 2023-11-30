package com.example.myapplication;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

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

    private Button scan_button;
    private Button galleryButton;

    private static final int GALLERY_REQUEST_CODE = 1669159;

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
        scan_button = view.findViewById(R.id.scan_button);
        galleryButton = view.findViewById(R.id.gallery);


        // BUTTON CLICK TRIGGERS ZXING BARCODE SCANNER
        // SUCCESSFUL SCAN TRIGGERS onACtivityResult
        scan_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AddItemFragment.this);
                integrator.setOrientationLocked(true);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
                integrator.setPrompt("Scan a barcode or QR code");
                integrator.initiateScan();
            }
        });

        // START THE IN BUILT GALLERY IMAGE PICKER ACTIVITY
        // PICKED IMAGE WILL TRIGGER THE onActivityResult method with reqCode = GALLERY_REQUEST_CODE
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, GALLERY_REQUEST_CODE);
            }
        });



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
                                description, make, model, serial, comment, tag);
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
        // BARCODE OBTAINED FROM GALLERY IMAGE
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            // Decode the barcode/QR code from the selected image
            decodeBarcodeFromGalleryImage(imageUri);
        }
        // UPDATE PRODUCT SERIAL AND DESCRIPTION FROM BARCODE
        // BARCODE OBTAINED FROM BARCODE SCAN FROM CAMERA
        else {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                if (result.getContents() == null) {
                    Toast.makeText(requireContext(), "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    String barCode = result.getContents();
                    Toast.makeText(requireContext(), "Scanned: " + barCode, Toast.LENGTH_LONG).show();

                    // Process the scanned barcode or QR code here
                    fetchProductInfo(barCode);
                    itemSerial.setText(result.getContents());
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }



    /**
     * Function extracts the barcode number and description frm a gallery image
     * Most code is boilerplate and generated by GPT 4.
     * Finally it sets item serial and description on UI
     *
     * @param imageUri Image picked from gallery
     */
    public void decodeBarcodeFromGalleryImage(Uri imageUri){
        try {
            // CONVERT IMAGE TO BITMAP
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
            int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            // FOCUS THE BITMAP AND MAKE IT BINARY
            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(),intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            // READ THE BARCODE AND GET BARCODE NUMBER
            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap);

            // UPDATE ITEM SERIAL WITH ITS BAR CODE OR GTIN NUMBER
            String barcode = result.getText();
            Toast.makeText(getContext(), "Decoded: " + barcode, Toast.LENGTH_LONG).show();
            itemSerial.setText(barcode);
            fetchProductInfo(barcode);

            // Process the result here
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Error: " + "decoding barcode", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Function fetches the product information from its barcode.
     * In doing so, the method makes an API call to a rapid GTIN API:
     *      // https://rapidapi.com/bigproductdata/api/big-product-data/
     * @param barcode barcode number of the product
     */
    public void fetchProductInfo(String barcode) {

        new Thread(() -> {

            HttpURLConnection urlConnection = null;
            try {
                // BUILT THE API REQUEST
                URL url = new URL("https://big-product-data.p.rapidapi.com/gtin/" + barcode);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key", "ad5c40df65mshba2972e2b4bd264p1df5f1jsnae362888b3bb");

                // GET THE API RESPONSE AS A STRING
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder result = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                reader.close();
                String responseString = result.toString();

                if (urlConnection != null) {
                    urlConnection.disconnect();
                }

                // SHOW APPROPRIATE TOASTS AND UPDATE ITEM DESCRIPTION
                getActivity().runOnUiThread(() -> {
                    Log.d("API response SUCCESSFUL", responseString);
                    String description = null;
                    try {
                        description = getDescriptionFromResponse(responseString);
                        Log.d("API response description", description);
                        itemDescription.setText(description);
                        Toast.makeText(requireContext(), "Retrieved product description", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d("API response", "Description not found");
                        itemDescription.setText("");
                        Toast.makeText(requireContext(), "Description not found", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                Log.d("API response ERROR", e.toString());
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(requireContext(), "Could not retrieve product description", Toast.LENGTH_SHORT).show();
                });

            }
        }).start();
    }


    /**
     * Method extracts description of item from the GTIN API response
     * @param responseData The string built from the rapid GTIN API response
     * @return (String) description of product extracted from the GTIN API response
     * @throws JSONException
     */
    public String getDescriptionFromResponse(String responseData) throws JSONException {

        // Parse the JSON response
        JSONObject jsonObject = jsonObject = new JSONObject(responseData);
        String description = jsonObject.getJSONObject("properties")
                .getJSONArray("description")
                .getString(0);
        Log.d("API response", description);
        return description;
    }
}
