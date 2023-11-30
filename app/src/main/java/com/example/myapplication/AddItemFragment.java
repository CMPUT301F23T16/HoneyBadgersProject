package com.example.myapplication;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.RequiresApi;
//import androidx.camera.core.CameraSelector;
//import androidx.camera.core.ExperimentalGetImage;
//import androidx.camera.core.ImageAnalysis;
//import androidx.camera.core.Preview;
//import androidx.camera.lifecycle.ProcessCameraProvider;
//import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;

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


    // DECLARE CAMERAX VARIABLES
//    private PreviewView previewView;
//    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
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


//        // SETUP CAMERAX CAMERA
//        previewView = view.findViewById(R.id.previewView);
//        cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext());
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                bindPreview(cameraProvider);
//            } catch (ExecutionException | InterruptedException e) {
//                // Handle exceptions
//            }
//        }, ContextCompat.getMainExecutor(requireContext()));



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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == getActivity().RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            // Decode the barcode/QR code from the selected image
            decodeBarcodeFromGalleryImage(imageUri);
        }
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

//    public void scanBarcodeZXING(){
//        IntentIntegrator integrator = new IntentIntegrator(this);
//        integrator.setCaptureActivity(ScannerActivity.class);
//        integrator.setOrientationLocked(false);
//        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
//        integrator.setPrompt("Scan a barcode or QR code");
//        integrator.initiateScan();
//    }

//    private void scanCode() {
//        ScanOptions options = new ScanOptions();
//        options.setOrientationLocked(false);
//        options.setPrompt("Scan a barcode");
//        options.setBeepEnabled(false);
//        options.setBarcodeImageEnabled(true);
//
//        // Start the scan
//        barcodeLauncher.launch(options);
//    }

//    public void scanBarcodeFromBitmap(Bitmap bitmap) {
//        InputImage image = InputImage.fromBitmap(bitmap, 0);
//        BarcodeScanner scanner = BarcodeScanning.getClient();
//
//        scanner.process(image).addOnSuccessListener(barcodes -> {
//            String rawValue = "";
//            for (Barcode barcode : barcodes) {
//                rawValue = barcode.getRawValue();
////                fetchProductInfo(rawValue);
//            }
//            itemSerial.setText(rawValue);
//        })
//        .addOnFailureListener(e -> {
//            Toast.makeText(requireContext(), "Could not scan barcode",
//                    Toast.LENGTH_SHORT).show();
//        });
//    }


    public void decodeBarcodeFromGalleryImage(Uri imageUri){
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imageUri);
            int[] intArray = new int[bitmap.getWidth()*bitmap.getHeight()];
            //copy pixel data from the Bitmap into the 'intArray' array
            bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

            LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(),intArray);
            BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));

            Reader reader = new MultiFormatReader();
            Result result = reader.decode(binaryBitmap);

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
//    public void selectImageFromGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);
//        intent.setType("image/*");
//        startActivityForResult(intent, GALLERY_REQUEST_CODE);
//    }

//    public Bitmap toGrayscale(Bitmap bmpOriginal) {
//        int width, height;
//        height = bmpOriginal.getHeight();
//        width = bmpOriginal.getWidth();
//
//        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//        Canvas c = new Canvas(bmpGrayscale);
//        Paint paint = new Paint();
//        ColorMatrix cm = new ColorMatrix();
//        cm.setSaturation(0);
//        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
//        paint.setColorFilter(f);
//        c.drawBitmap(bmpOriginal, 0, 0, paint);
//        return bmpGrayscale;
//    }
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
//            Uri selectedImageUri = data.getData();
//            try {
//                Bitmap bitmap = MediaStore.Images.Media
//                        .getBitmap(requireActivity().getContentResolver(), selectedImageUri);
//                scanBarcodeFromBitmap(toGrayscale(bitmap));
//            } catch (IOException e) {
//                e.printStackTrace();
//                Toast.makeText(requireContext(), "Could not load image", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


    public void fetchProductInfo(String barcode) {
        // https://rapidapi.com/bigproductdata/api/big-product-data/
        new Thread(() -> {

            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("https://big-product-data.p.rapidapi.com/gtin/" + barcode);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setRequestProperty("X-RapidAPI-Key", "ad5c40df65mshba2972e2b4bd264p1df5f1jsnae362888b3bb");
//                urlConnection.setRequestProperty("X-RapidAPI-Host", "big-product-data.p.rapidapi.com");

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




//    @OptIn(markerClass = ExperimentalGetImage.class)
//    private void bindPreview(ProcessCameraProvider cameraProvider) {
//        Preview preview = new Preview.Builder().build();
//        CameraSelector cameraSelector = new CameraSelector.Builder()
//                .requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
//        preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//        ImageAnalysis imageAnalysis =
//                new ImageAnalysis.Builder()
//                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
//                        .build();
//
//        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(requireContext()), imageProxy -> {
//            InputImage image = InputImage.fromMediaImage(imageProxy.getImage(), imageProxy.getImageInfo().getRotationDegrees());
//            BarcodeScanning.getClient().process(image)
//                    .addOnSuccessListener(barcodes -> {
//                        for (Barcode barcode : barcodes) {
//                            String rawValue = barcode.getRawValue();
//                            // Process the barcode data here
//                        }
//                    })
//                    .addOnFailureListener(e -> {
//                        // Handle the error
//                    })
//                    .addOnCompleteListener(task -> imageProxy.close());
//        });
//
//        cameraProvider.bindToLifecycle((LifecycleOwner)this, cameraSelector, preview, imageAnalysis);
//    }

//    private void startCamera() {
//        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);
//        cameraProviderFuture.addListener(() -> {
//            try {
//                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();
//                Preview preview = new Preview.Builder().build();
//                preview.setSurfaceProvider(previewView.getSurfaceProvider());
//
//                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
//
//                cameraProvider.unbindAll();
//                cameraProvider.bindToLifecycle(requireContext(), cameraSelector, preview);
//            } catch (Exception e) {
//                Log.e(TAG, "Use case binding failed", e);
//            }
//        }, ContextCompat.getMainExecutor(this));
//    }


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
