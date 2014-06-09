package com.andy.photogallery;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.andy.photogallery.model.GalleryItem;
import com.andy.photogallery.model.GalleryItemRepository;
import com.andy.photogallery.service.BitmapCache;
import com.andy.photogallery.service.ThumbnailDownloader;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "fragment";

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;
    private ThumbnailDownloader<ImageView> mThumbnailThread;
    private BitmapCache mCache;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        mCache = BitmapCache.getInstance();

        new FetchItemsTask().execute();

        mThumbnailThread = new ThumbnailDownloader<ImageView>(new Handler());
        mThumbnailThread.setListener(new ThumbnailDownloader.Listener<ImageView>() {
            @Override
            public void onThumbnailDownloaded(ImageView imageView, Bitmap thumbnail) {
                if (isVisible()) {
                    imageView.setImageBitmap(thumbnail);
                    Log.i(TAG, "Tag: " + imageView.getTag());
                    mCache.add(imageView.getTag().toString(), thumbnail);
                }
            }
        });
        mThumbnailThread.start();
        mThumbnailThread.getLooper();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mThumbnailThread.quit();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mThumbnailThread.clearQueue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_gallery, container, false);
        mGridView = (GridView) v.findViewById(R.id.grid_view);
        initAdapter();
        return v;
    }

    private class FetchItemsTask extends AsyncTask<Void, Void, ArrayList<GalleryItem>> {
        @Override
        protected ArrayList<GalleryItem> doInBackground(Void... params) {
            return new GalleryItemRepository().getItems();
        }
        @Override
        protected void onPostExecute(ArrayList<GalleryItem> items) {
            mItems = items;
            initAdapter();
        }
    }

    private void initAdapter() {
        if (getActivity() == null || mGridView == null) {
            return;
        }
        if (mItems != null) {
            mGridView.setAdapter(new GalleryItemAdapter(mItems));
        } else {
            mGridView.setAdapter(null);
        }
    }

    private class GalleryItemAdapter extends ArrayAdapter<GalleryItem> {

        public GalleryItemAdapter(ArrayList<GalleryItem> items) {
            super(getActivity(), 0, items);
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {

            Log.i(TAG, "getView() pos: " + position);

            if (view == null) {
                LayoutInflater inflater = getActivity().getLayoutInflater();
                view = inflater.inflate(R.layout.gallery_item, parent, false);
            }

            ImageView imageView = (ImageView) view.findViewById(R.id.gallery_item_image);

            if (mCache.contains(Integer.toString(position))) {
                imageView.setImageBitmap(mCache.get(Integer.toString(position)));
                return view;
            }

            imageView.setImageResource(R.drawable.kermit);
            imageView.setTag(position);

            GalleryItem item = getItem(position);
            mThumbnailThread.queueThumbnail(imageView, item.getUrl());

            return view;
        }
    }
}