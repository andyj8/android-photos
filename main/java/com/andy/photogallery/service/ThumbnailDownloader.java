package com.andy.photogallery.service;

import android.os.HandlerThread;
import android.util.Log;

public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";

    public ThumbnailDownloader(String name) {
        super(name);
    }

    public void queueThumbnail(Token token, String url) {
        Log.i(TAG, "Got an URL: " + url);
    }

}
