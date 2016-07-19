package com.prashantmaurice.android.mediapicker.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
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

    public void loadImage(MediaObj mediaObj, ImageView imageview, Activity activity){
        imageview.setTag(R.string.tag_imagetoload, mediaObj.getPath());
        if(cache.containsBitmap(mediaObj)){
            imageview.setImageBitmap(cache.getBitmap(mediaObj));
        }else{
            imageview.setImageResource(R.drawable.empty);
            BitmapWorkerTask task = new BitmapWorkerTask(imageview,activity);

            //remove any pending image load tasks
            if(imageview.getTag(R.string.tag_bitmaploadertask)!=null){
                BitmapWorkerTask previoustask = (BitmapWorkerTask) imageview.getTag(R.string.tag_bitmaploadertask);
                previoustask.cancel(true);
            }

            imageview.setTag(R.string.tag_bitmaploadertask,task);
            task.execute(mediaObj);
        }
    }

    public void flushCache() {
        cache.flushEverything();
//        cache = new Cache();
    }

    //Load images in Background
    class BitmapWorkerTask extends AsyncTask<MediaObj, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private final Activity activity;
        private MediaObj mediaObj;

        public BitmapWorkerTask(ImageView imageView, Activity activity) {
            this.activity = activity;

            // Use a WeakReference to ensure the ImageView can be garbage collected
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        // Decode image in background.
        @Override
        protected Bitmap doInBackground(MediaObj... params) {
            mediaObj = params[0];

            try {
                if (mediaObj.getMediaType() == MediaPicker.Pick.VIDEO) {
                    Bitmap bitmapOrg = MediaStore.Video.Thumbnails.getThumbnail(activity.getContentResolver(), mediaObj.getMediaId(), MediaStore.Video.Thumbnails.MINI_KIND, null);
                    return bitmapOrg;
                } else if (mediaObj.getMediaType() == MediaPicker.Pick.IMAGE) {
                    Bitmap bitmapOrg = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), mediaObj.getMediaId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
                    MImageObj mImageObj = (MImageObj) mediaObj;
                    if (mImageObj.getOrientation() == 0) return bitmapOrg;
                    Matrix matrix = new Matrix();
                    matrix.postRotate(mImageObj.getOrientation());
                    Bitmap bitmapRotated = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
                    if(bitmapOrg != null && !bitmapOrg.isRecycled()) bitmapOrg.recycle();
                    return bitmapRotated;
                } else {
                    return null;
                }
            } catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }


        // Once complete, see if ImageView is still around and set bitmap.
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                cache.storeInCache(mediaObj,bitmap);
                final ImageView imageView = imageViewReference.get();
                if (imageView != null && mediaObj.getMainUri().getPath().equals(imageView.getTag(R.string.tag_imagetoload))) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new java.net.URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static class Cache{
        static final String TAG = "CACHE";
        final static int CACHE_SIZE_LIMIT = 30;
        private Map<String, Bitmap> cacheData = new HashMap<>();
        private Stack<String> usedQueue = new Stack<>();

        public boolean containsBitmap(MediaObj mediaObj){
            return cacheData.containsKey(mediaObj.getMainUri().getPath());
        }

        public Bitmap getBitmap(MediaObj mediaObj) {
            Logg.d(TAG,"Used from cache : "+ mediaObj.getPath());
            usedQueue.remove(mediaObj.getMainUri().getPath());
            usedQueue.add(0, mediaObj.getMainUri().getPath());
            return cacheData.get(mediaObj.getMainUri().getPath());
        }

        public void storeInCache(MediaObj mediaObj, Bitmap bitmap) {
            //Remove least used resource
            if(!usedQueue.isEmpty() && usedQueue.size()>CACHE_SIZE_LIMIT && !usedQueue.contains(mediaObj.getPath())){
                String toremove = usedQueue.pop();
                Logg.d(TAG,"Removed from cache : "+toremove);
                Bitmap bm = cacheData.remove(toremove);
//                bm.recycle();

            }

            //add this
            Logg.d(TAG,"Stored in cache : "+ mediaObj.getPath());
            usedQueue.add(0, mediaObj.getPath());
            cacheData.put(mediaObj.getPath(),bitmap);
        }

        public void flushEverything(){
//            for(String key : cacheData.keySet()){
//                cacheData.get(key).recycle();
//            }
            cacheData.clear();
            usedQueue.clear();
        }
    }


}
