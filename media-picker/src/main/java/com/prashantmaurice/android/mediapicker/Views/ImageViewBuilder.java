package com.prashantmaurice.android.mediapicker.Views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;
import com.prashantmaurice.android.mediapicker.Utils.Logg;

import java.io.File;

/**
 * Documented by maurice :
 *
 * This is basic builder for getting a open groups view which can be used to inflate in Explore open group pages
 */
public class ImageViewBuilder {
    static final String TAG = "FOLDERVIEW";


    public static View getView(Activity activity){
        LayoutInflater inflator = LayoutInflater.from(activity);
        View mainView = inflator.inflate(R.layout.adapterview_image, null);
        ViewHolder holder = new ViewHolder(mainView, activity);
        mainView.setTag(holder);

        return mainView;
                
    }


    public static class ViewHolder{
        public View mainView;
        public ImageView imageview;

        Activity activity;

        public ViewHolder(View itemView, Activity activity) {
            this.activity = activity;
            mainView = itemView;
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
        }




        public void inflateData(final ImageObj folderObj){
            Logg.d(TAG, "Inflating data in ImageObj view");

            File imgFile = new File(folderObj.getPath());
            if(imgFile.exists()){

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                imageview.setImageBitmap(myBitmap);

            }

        }


        public void loadImage(ImageObj imageObj) {
            BitmapLoaderController.getInstance().loadImage(imageObj,imageview, activity);
        }
    }


}
