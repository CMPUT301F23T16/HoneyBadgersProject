package com.example.myapplication;



import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.Calendar;

public class PhotosFragment extends DialogFragment {
    private FloatingActionButton photo_delete_button;
    private FloatingActionButton photo_camera_button;
    private FloatingActionButton photo_gallery_button;
    private GridView grid_view;
    private PhotosInteractionInterface listener;
    private Uri current_uri;
    private PhotoArrayAdapter photo_adapter;


    public interface PhotosInteractionInterface{
        //public static final int CAMERA_REQUEST_CODE = 1;
        public File getDirectory();
        public void addPhoto(Uri uri);
        public PhotoArrayAdapter getGridAdapter();
        public void resetPhotos();

    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof AddItemFragment.AddItemInteractionInterface) {
            listener = (PhotosInteractionInterface) context;
        }
        else {
            throw new RuntimeException(context.toString() + "OnFragmentInteractionListener is not implemented");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.photos_fragment, null);
        photo_delete_button = view.findViewById(R.id.photoDeleteButton);
        photo_camera_button = view.findViewById(R.id.photoCameraButton);
        photo_gallery_button = view.findViewById(R.id.photoGalleryButton);
        grid_view = view.findViewById(R.id.photo_grid);
        photo_adapter = listener.getGridAdapter();
        grid_view.setAdapter(photo_adapter);



        photo_camera_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.CAMERA)==PackageManager.PERMISSION_DENIED)
                {
                    requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA);

                }
               else {
                   openCamera();
               }

            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Photos")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().onBackPressed(); //to be changed
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.resetPhotos();

                    }
                })
        ;
        return builder.create();


    }
    private void openCamera()
    {

        Log.d("Before Creating File", "openCamera: ");
        File file = new File(listener.getDirectory(), Calendar.getInstance().getTimeInMillis()+".png");

        current_uri = MyFileProvider.getUriForFile(requireContext(), requireContext().getApplicationContext().getPackageName() + ".provider", file);

        Log.d("After Creating File", "openCamera: ");
        takePicture.launch(current_uri);

    }


    private ActivityResultLauncher<String> requestCameraPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {
                    if (result) {
                        openCamera();
                    } else {
                        Toast.makeText(requireContext(), "Camera Permission is Required to Take Photos", Toast.LENGTH_LONG).show();
                    }
                }
            }
    );


    ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    // do what you need with the uri here ...
                    listener.addPhoto(current_uri);
                    photo_adapter.notifyDataSetChanged();

                }
            });



}
