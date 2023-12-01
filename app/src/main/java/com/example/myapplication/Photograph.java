package com.example.myapplication;

import android.net.Uri;

public class Photograph {
    private Uri uri;
    public Photograph(Uri uri)
    {
        this.uri = uri;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
