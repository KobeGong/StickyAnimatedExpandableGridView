package com.baidu.stickyheadergridview.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.baidu.stickyheadergridview.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by kobe-mac on 15/11/28.
 */
public class BitmapLoadTask extends AsyncTask<Void, Integer, Bitmap> {

    static LruCache<String, Bitmap> memCache;
    private WeakReference<ImageView> mImageRef;
    private String url;

    static {
        memCache = new LruCache<>((int) (Runtime.getRuntime().maxMemory()/8));
    }

    public BitmapLoadTask(ImageView imageView) {
        mImageRef = new WeakReference<ImageView>(imageView);
        if(!validateImageRef()){
            url = null;
        } else{
            url = (String) mImageRef.get().getTag(R.id.load_mark_url);
        }
    }

    private boolean validateImageRef(){
        return mImageRef != null && (mImageRef.get() != null);
    }

    @Override
    protected void onPreExecute() {
        if(validateImageRef()){
            mImageRef.get().setImageDrawable(new ColorDrawable(Color.WHITE));
        }
    }

    @Override
    protected Bitmap doInBackground(Void... param) {

        if(url == null || url.isEmpty()){
            return null;
        }
        Bitmap bitmap = memCache.get(url);
        if(bitmap != null){
            return bitmap;
        }
        try {
            URLConnection conn = new URL(url).openConnection();
            InputStream in = conn.getInputStream();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int b;
            while((b = in.read()) != -1){
                out.write(b);
            }
            byte[] data = out.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            memCache.put(url, bitmap);
            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if(validateImageRef()){
            mImageRef.get().setTag(R.id.load_mark_loading, false);
            if(bitmap != null && mImageRef.get().getTag(R.id.load_mark_url) == url) {
                mImageRef.get().setImageBitmap(bitmap);
            }
        }
    }
}
