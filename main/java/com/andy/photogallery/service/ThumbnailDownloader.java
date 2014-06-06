package com.andy.photogallery.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ThumbnailDownloader<Token> extends HandlerThread {

    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private Handler mHandler;
    private Handler mResponseHandler;
    private Listener<Token> mListener;

    private Map<Token, String> imageTokenToUrlMap =
            Collections.synchronizedMap(new HashMap<Token, String>());

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    public interface Listener<Token> {
        void onThumbnailDownloaded(Token token, Bitmap thumbnail);
    }

    public void setListener(Listener<Token> listener) {
        mListener = listener;
    }

    public void queueThumbnail(Token token, String url) {
        imageTokenToUrlMap.put(token, url);
        mHandler.obtainMessage(MESSAGE_DOWNLOAD, token).sendToTarget();
    }

    public void clearQueue() {
        mHandler.removeMessages(MESSAGE_DOWNLOAD);
        imageTokenToUrlMap.clear();
    }

    @Override
    protected void onLooperPrepared() {
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    @SuppressWarnings("unchecked")
                    Token token = (Token) msg.obj;
                    Log.i(TAG, "Got a request for url: " + imageTokenToUrlMap.get(token));
                    handleRequest(token);
                }
            }
        };
    }

    private void handleRequest(final Token token) {
        try {
            final String url = imageTokenToUrlMap.get(token);
            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetcher().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);

            mResponseHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (imageTokenToUrlMap.get(token) != url) {
                        return;
                    }
                    imageTokenToUrlMap.remove(token);
                    mListener.onThumbnailDownloaded(token, bitmap);
                }
            });

        } catch (IOException e) {
            Log.e(TAG, "Error downloading image", e);
            e.printStackTrace();
        }
    }

}
