package com.baidu.stickyheadergridview.network;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.widget.ImageView;

import com.baidu.stickyheadergridview.R;

/**
 * Created by kobe-mac on 15/11/28.
 */
public class ImageLoader {

    public static void load(String url, ImageView imageView) {
        Boolean loading = (Boolean) imageView.getTag(R.id.load_mark_loading);
        if(loading == null){
            loading = Boolean.FALSE;
        }
        if(loading){
            String loadingUrl = (String) imageView.getTag(R.id.load_mark_url);
            if(TextUtils.equals(loadingUrl,  url)){
                return;
            }
        }
        Bitmap bitmap = BitmapLoadTask.memCache.get(url);
        imageView.setTag(R.id.load_mark_url, url);
        if(bitmap != null){
            imageView.setImageBitmap(bitmap);
            return;
        }
        imageView.setTag(R.id.load_mark_loading, true);
        new BitmapLoadTask(imageView).execute();
    }

}
