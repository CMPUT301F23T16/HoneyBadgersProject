package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PhotoArrayAdapter extends ArrayAdapter<ImageView> {
    private ArrayList<ImageView> photos;
    private Context context;

    public PhotoArrayAdapter(Context context, ArrayList<ImageView> photos)
    {
        super(context,0,photos);
        this.photos = photos;
        this.context=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       photos.get(position).setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 1000));

       return photos.get(position);
    }
}
