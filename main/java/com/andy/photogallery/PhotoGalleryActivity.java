package com.andy.photogallery;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

public class PhotoGalleryActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_gallery);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PhotoGalleryFragment())
                    .commit();
        }
    }

}
