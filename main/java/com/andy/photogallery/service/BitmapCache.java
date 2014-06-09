package com.andy.photogallery.service;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class BitmapCache extends LruCache<String, Bitmap> {

    public static BitmapCache getInstance() {
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = maxMemory / 8;

        return new BitmapCache(cacheSize);
    }

    private BitmapCache(int maxSize) {
        super(maxSize);
    }

    public boolean contains(String key) {
        return get(key) != null;
    }

    public void add(String key, Bitmap bitmap) {
        if (get(key) == null) {
            put(key, bitmap);
        }
    }

}
