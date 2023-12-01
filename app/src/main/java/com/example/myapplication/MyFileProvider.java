package com.example.myapplication;

import androidx.core.content.FileProvider;

public class MyFileProvider extends FileProvider {
    public MyFileProvider()
    {
        super(R.xml.file_paths);
    }

}
