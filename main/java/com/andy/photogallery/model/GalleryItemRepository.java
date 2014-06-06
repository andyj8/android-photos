package com.andy.photogallery.model;

import com.andy.photogallery.service.FlickrFetcher;
import com.andy.photogallery.service.FlickrParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class GalleryItemRepository {

    public ArrayList<GalleryItem> getItems() {
        ArrayList<GalleryItem> items = new ArrayList<GalleryItem>();

        try {
            new FlickrParser().parseXml(new FlickrFetcher().fetchXml(), items);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return items;
    }

}
