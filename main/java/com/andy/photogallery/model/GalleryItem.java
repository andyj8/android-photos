package com.andy.photogallery.model;

import java.net.MalformedURLException;
import java.net.URL;

public class GalleryItem {

    private String id;
    private String url;
    private String caption;
    private String file;

    public GalleryItem(String id, String caption, String url) {
        this.id = id;
        this.caption = caption;
        this.url = url;

        try {
            URL urlInfo = new URL(getUrl());
            this.file = urlInfo.getFile();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getFile() {
        return file;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        return caption;
    }



}
