package com.andy.photogallery.model;

public class GalleryItem {

    private String id;
    private String url;
    private String caption;

    public GalleryItem(String id, String url, String caption) {
        this.id = id;
        this.url = url;
        this.caption = caption;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public String getCaption() {
        return caption;
    }

    public String toString() {
        return caption;
    }

}
