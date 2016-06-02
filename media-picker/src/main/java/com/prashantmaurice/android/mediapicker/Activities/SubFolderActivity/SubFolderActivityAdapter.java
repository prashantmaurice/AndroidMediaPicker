package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Views.ImageViewBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 01/09/15.
 */
public class SubFolderActivityAdapter extends BaseAdapter {
    private final List<ImageObj> folders = new ArrayList<>();
    private final SubFolderActivity activity;

    public SubFolderActivityAdapter(SubFolderActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public ImageObj getItem(int position) {
        return folders.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //initialize view
        View view;
        if (convertView == null) {
            view = ImageViewBuilder.getView(activity);
        }
        else {
            view = convertView;
        }

        ImageViewBuilder.ViewHolder holder = (ImageViewBuilder.ViewHolder) view.getTag();


        //set view
        final ImageObj imageObj = folders.get(position);
        loadImage(imageObj,holder.imageview);

        return view;
    }

    private void loadImage(ImageObj imageObj, ImageView imageview){
        imageview.setImageResource(R.drawable.empty);
        imageview.setTag(R.string.tag_imagetoload,imageObj.getPath());
        BitmapWorkerTask task = new BitmapWorkerTask(imageview);
        task.execute(imageObj);
    }


    public void setData(List<ImageObj> data) {
        folders.clear();
        folders.addAll(data);
    }

    //Load images in Background
    class BitmapWorkerTask extends AsyncTask<ImageObj, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;
        private ImageObj imageObj;

        public BitmapWorkerTask(ImageView imageView) {
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
            if (imageViewReference != null && bitmap != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null && imageObj.getPath().equals(imageView.getTag(R.string.tag_imagetoload))) {
                    imageView.setImageBitmap(bitmap);
                }
            }
        }
    }
}
