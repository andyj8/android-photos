package com.andy.photogallery.service;

import com.andy.photogallery.model.GalleryItem;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class FlickrParser {

    private static final String XML_PHOTO = "photo";
    private static final String FIELD_ID = "id";
    private static final String FIELD_TITLE = "title";
    private static final String FIELD_SMALL_URL = "url_s";

    private XmlPullParser parser;

    public FlickrParser() throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        parser = factory.newPullParser();
    }

    public void parseXml(String xmlString, ArrayList<GalleryItem> items)
            throws XmlPullParserException, IOException {

        parser.setInput(new StringReader(xmlString));

        int eventType = parser.next();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && XML_PHOTO.equals(parser.getName())) {

                String id = parser.getAttributeValue(null, FIELD_ID);
                String caption = parser.getAttributeValue(null, FIELD_TITLE);
                String smallUrl = parser.getAttributeValue(null, FIELD_SMALL_URL);

                items.add(new GalleryItem(id, caption, smallUrl));
            }

            eventType = parser.next();
        }
    }

}
