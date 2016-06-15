package com.prashantmaurice.android.mediapicker.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;
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

    public void loadImage(MImageObj MImageObj, ImageView imageview, Activity activity){
        imageview.setTag(R.string.tag_imagetoload, MImageObj.getPath());
        if(cache.containsBitmap(MImageObj)){
            imageview.setImageBitmap(cache.getBitmap(MImageObj));
        }else{
            imageview.setImageResource(R.drawable.empty);
            BitmapWorkerTask task = new BitmapWorkerTask(imageview,activity);

            //remove any pending image load tasks
            if(imageview.getTag(R.string.tag_bitmaploadertask)!=null){
                BitmapWorkerTask previoustask = (BitmapWorkerTask) imageview.getTag(R.string.tag_bitmaploadertask);
                previoustask.cancel(true);
            }

            imageview.setTag(R.string.tag_bitmaploadertask,task);
            task.execute(MImageObj);
        }
    }


    //Load images in Background
    class BitmapWorkerTask extends AsyncTask<MImageObj, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final Activity activity;
        private MImageObj mImageObj;

        public BitmapWorkerTask(ImageView imageView, Activity activity) {
            this.activity = activity;

            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(MImageObj... params) {
            mImageObj = params[0];
            return MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), mImageObj.getImageId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
        }


        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                cache.storeInCache(mImageObj,bitmap);
                final ImageView imageView = imageViewReference.get();
                if (imageView != null && mImageObj.getMainUri().getPath().equals(imageView.getTag(R.string.tag_imagetoload))) {
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

        public boolean containsBitmap(MImageObj MImageObj){
            return cacheData.containsKey(MImageObj.getMainUri().getPath());
        }

        public Bitmap getBitmap(MImageObj MImageObj) {
            Logg.d(TAG,"Used from cache : "+ MImageObj.getPath());
            usedQueue.remove(MImageObj.getMainUri().getPath());
            usedQueue.add(0, MImageObj.getMainUri().getPath());
            return cacheData.get(MImageObj.getMainUri().getPath());
        }

        public void storeInCache(MImageObj mImageObj, Bitmap bitmap) {
            //Remove least used resource
            if(!usedQueue.isEmpty() && usedQueue.size()>CACHE_SIZE_LIMIT && !usedQueue.contains(mImageObj.getPath())){
                String toremove = usedQueue.pop();
                Logg.d(TAG,"Removed from cache : "+toremove);
                cacheData.remove(toremove);
            }

            //add this
            Logg.d(TAG,"Stored in cache : "+ mImageObj.getPath());
            usedQueue.add(0, mImageObj.getPath());
            cacheData.put(mImageObj.getPath(),bitmap);
        }
    }


}
