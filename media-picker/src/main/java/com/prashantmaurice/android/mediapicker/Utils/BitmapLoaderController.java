package com.prashantmaurice.android.mediapicker.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by maurice on 02/06/16.
 */
public class BitmapLoaderController {
    static BitmapLoaderController instance;
    Cache cache;

    public static BitmapLoaderController getInstance(){
        if(instance==null) instance = new BitmapLoaderController();
        return instance;
    }

    BitmapLoaderController(){
        cache = new Cache();
    }

    public void loadImage(ImageObj imageObj, ImageView imageview, Activity activity){
        imageview.setTag(R.string.tag_imagetoload,imageObj.getPath());
        if(cache.containsBitmap(imageObj)){
            imageview.setImageBitmap(cache.getBitmap(imageObj));
        }else{
            imageview.setImageResource(R.drawable.empty);
            BitmapWorkerTask task = new BitmapWorkerTask(imageview,activity);

            //remove any pending tasks
            if(imageview.getTag(R.string.tag_bitmaploadertask)!=null){
                BitmapWorkerTask previoustask = (BitmapWorkerTask) imageview.getTag(R.string.tag_bitmaploadertask);
                previoustask.cancel(true);
            }

            imageview.setTag(R.string.tag_bitmaploadertask,task);
            task.execute(imageObj);
        }
    }


    //Load images in Background
    class BitmapWorkerTask extends AsyncTask<ImageObj, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final Activity activity;
        private ImageObj imageObj;

        public BitmapWorkerTask(ImageView imageView, Activity activity) {
            this.activity = activity;

            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(ImageObj... params) {
            imageObj = params[0];
            return MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), imageObj.getId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
        }


        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                cache.storeInCache(imageObj,bitmap);
                final ImageView imageView = imageViewReference.get();
                if (imageView != null && imageObj.getPath().equals(imageView.getTag(R.string.tag_imagetoload))) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    private static class Cache{
        static final String TAG = "CACHE";
        final static int CACHE_SIZE_LIMIT = 30;
        private Map<String, Bitmap> cacheData = new HashMap<>();
        private Stack<String> usedQueue = new Stack<>();

        public boolean containsBitmap(ImageObj imageObj){
            return cacheData.containsKey(imageObj.getPath());
        }

        public Bitmap getBitmap(ImageObj imageObj) {
            Logg.d(TAG,"Used from cache : "+imageObj.getPath());
            usedQueue.remove(imageObj.getPath());
            usedQueue.add(0,imageObj.getPath());
            return cacheData.get(imageObj.getPath());
        }

        public void storeInCache(ImageObj imageObj, Bitmap bitmap) {
            //Remove least used resource
            if(!usedQueue.isEmpty() && usedQueue.size()>CACHE_SIZE_LIMIT && !usedQueue.contains(imageObj.getPath())){
                String toremove = usedQueue.pop();
                Logg.d(TAG,"Removed from cache : "+toremove);
                cacheData.remove(toremove);
            }

            //add this
            Logg.d(TAG,"Stored in cache : "+imageObj.getPath());
            usedQueue.add(0,imageObj.getPath());
            cacheData.put(imageObj.getPath(),bitmap);
        }
    }


}
