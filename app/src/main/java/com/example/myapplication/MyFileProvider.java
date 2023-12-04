package com.example.myapplication;

import androidx.core.content.FileProvider;

/**
 * Class for MyFileProvider extends FileProvider
 */
public class MyFileProvider extends FileProvider {
    /** Constructor method for class
     *
     */
    public MyFileProvider()
    {
        super(R.xml.file_paths);
    }

}
