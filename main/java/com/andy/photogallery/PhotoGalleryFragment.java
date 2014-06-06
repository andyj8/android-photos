package com.andy.photogallery;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import com.andy.photogallery.model.GalleryItem;
import com.andy.photogallery.model.GalleryItemRepository;

import java.util.ArrayList;

public class PhotoGalleryFragment extends Fragment {

    private GridView mGridView;
    private ArrayList<GalleryItem> mItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        new FetchItemsTask().execute();
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
            mGridView.setAdapter(
                new ArrayAdapter<GalleryItem>(
                    getActivity(), android.R.layout.simple_gallery_item, mItems
                )
            );
        } else {
            mGridView.setAdapter(null);
        }
    }

}