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
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
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
    private FloatingActionButton photo_camera_button;
    private FloatingActionButton photo_gallery_button;
    private GridView grid_view;
    private PhotosInteractionInterface listener;
    private Uri current_uri;
    private PhotoArrayAdapter photo_adapter;

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


    private ActivityResultLauncher<Uri> takePicture = registerForActivityResult(
            new ActivityResultContracts.TakePicture(),
            new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean result) {

                    // do what you need with the uri here ...
                    listener.addPhoto(current_uri);
                    photo_adapter.notifyDataSetChanged();
                    current_uri = null;

                }
            });
    private ActivityResultLauncher<PickVisualMediaRequest> pickMultipleMedia =
            registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(5), uris -> {
                // Callback is invoked after the user selects media items or closes the
                // photo picker.
                if (!uris.isEmpty()) {
                    Log.d("PhotoPicker", "Number of items selected: " + uris.size());
                    for(Uri uri:uris)
                    {
                        listener.addPhoto(uri);
                    }
                    photo_adapter.notifyDataSetChanged();
                } else {
                    Log.d("PhotoPicker", "No media selected");
                }
            });




    public interface PhotosInteractionInterface{
        //public static final int CAMERA_REQUEST_CODE = 1;
        public File getDirectory();
        public void addPhoto(Uri uri);
        public PhotoArrayAdapter getGridAdapter();
        public void resetPhotos();
        public void removePhoto(int pos);
        public void setReloadingImagesToTrue();
        public boolean getEditingItem();
        public Item getTemporaryState();

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

    @Override
    public void onResume() {
        photo_adapter.notifyDataSetChanged();
        super.onResume();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.photos_fragment, null);
        photo_camera_button = view.findViewById(R.id.photoCameraButton);
        photo_gallery_button = view.findViewById(R.id.photoGalleryButton);
        grid_view = view.findViewById(R.id.photo_grid);
        photo_adapter = listener.getGridAdapter();
        grid_view.setAdapter(photo_adapter);
        photo_adapter.notifyDataSetChanged();



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
        photo_gallery_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickMultipleMedia.launch(new PickVisualMediaRequest.Builder()
                        .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                        .build());

            }
        });

        /**  To enlarge image has bugs
        grid_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Dialog builder =  new Dialog(requireContext(), android.R.style.Theme_Light);
                builder.requestWindowFeature(Window.FEATURE_NO_TITLE);
                builder.getWindow().setBackgroundDrawable(
                        new ColorDrawable(android.graphics.Color.TRANSPARENT));
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        //nothing;
                        view.setLayoutParams(new AbsListView.LayoutParams(AbsListView.LayoutParams.WRAP_CONTENT, 1000));
                    }
                });


                builder.addContentView(view, new RelativeLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));
                builder.show();
            }
        });
         */
        grid_view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog dialog = new AlertDialog.Builder(requireContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete the selected image?")
                        .setPositiveButton("Delete", (dialogInterface, which) -> {

                            listener.removePhoto(position);
                            photo_adapter.notifyDataSetChanged();



                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
                return true;
            }
        });
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setView(view)
                .setTitle("Photos")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.setReloadingImagesToTrue();
                        if(listener.getEditingItem())
                            EditItemFragment.newInstance(listener.getTemporaryState()).show(getActivity().getSupportFragmentManager(), "Edit Item");
                        else
                            new AddItemFragment().show(getActivity().getSupportFragmentManager(), "ADD_ITEM");
                    }
                }) //to be changed
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.resetPhotos();
                        if(listener.getEditingItem())
                            EditItemFragment.newInstance(listener.getTemporaryState()).show(getActivity().getSupportFragmentManager(), "Edit Item");
                        else
                            new AddItemFragment().show(getActivity().getSupportFragmentManager(), "ADD_ITEM");
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



}
