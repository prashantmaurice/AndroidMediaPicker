package com.prashantmaurice.android.mediapicker.Views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity.SubFolderActivity;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;
import com.prashantmaurice.android.mediapicker.Utils.PicassoUtils;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;
import com.squareup.picasso.MemoryPolicy;

/**
 * Documented by maurice :
 *
 * This is basic builder for getting a open groups view which can be used to inflate in Explore open group pages
 */
public class ImageViewBuilder {
    static final String TAG = "FOLDERVIEW";


    public static View getView(SubFolderActivity activity){
        LayoutInflater inflator = LayoutInflater.from(activity);
        View mainView = inflator.inflate(R.layout.adapterview_image, null);
        ViewHolder holder = new ViewHolder(mainView, activity);
        mainView.setTag(holder);

        return mainView;
                
    }


    public static class ViewHolder{
        public View mainView, cont_select_overlay,main_cont;
        public ImageView imageview, image_overlay;
        public TextView tv_number;

        SubFolderActivity activity;

        public ViewHolder(View itemView, SubFolderActivity activity) {
            this.activity = activity;
            mainView = itemView;
            main_cont =  itemView.findViewById(R.id.main_cont);
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            tv_number = (TextView) itemView.findViewById(R.id.tv_number);
            cont_select_overlay = itemView.findViewById(R.id.cont_select_overlay);
            image_overlay = (ImageView)itemView.findViewById(R.id.overlay);
        }




//        public void inflateData(final MImageObj folderObj){
//            Logg.d(TAG, "Inflating data in ImageObj view");
//
//            File imgFile = new File(folderObj.getThumbPath());
//            if(imgFile.exists()){
//
//                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
//                imageview.setImageBitmap(myBitmap);
//
//            }
//
//        }


        public void loadImage(MediaObj mediaObj) {

            switch (mediaObj.getMediaType()){
                case VIDEO:
                    if(imageview.getTag()==null || !imageview.getTag().equals("V:"+mediaObj.getMediaId()) || imageview.getDrawable() == null){
                        activity.getPicassoForVideo()
                                .load(PicassoUtils.VideoThumbnailRequestHandler.SCHEME +"://"+mediaObj.getMediaId())
                                .into(imageview);
                        imageview.setTag("V:"+mediaObj.getMediaId());
                    }

//                        Uri videoThumbUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.toString(mediaObj.getMediaId()));
//                        Picasso.with(activity)
//                                .load(videoThumbUri)
//                                .into(imageview);
                    break;


                case IMAGE:

                    MImageObj imageObj = (MImageObj) mediaObj;
                    if(imageview.getTag()==null || !imageview.getTag().equals("I:"+mediaObj.getMediaId()) || imageview.getDrawable() == null) {
                        activity.getPicassoForImage()
                                .load(PicassoUtils.ImageThumbnailRequestHandler.getUriRequest(mediaObj.getMediaId(), imageObj.getOrientation()))
                                .into(imageview);
                        imageview.setTag("I:"+mediaObj.getMediaId());
                    }


//                        Uri imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(mediaObj.getMediaId()));
//                        Picasso.with(activity)
//                                .load(imageURI)
//                                .into(imageview);
                    break;
            }
//
//
//            MImageObj imageObj = (MImageObj) mediaObj;
//            activity.getPicassoForImage()
//                    .load(PicassoUtils.ImageThumbnailRequestHandler.getUriRequest(mediaObj.getMediaId(),imageObj.getOrientation()))
//                    .into(imageview);


//            BitmapLoaderController.getInstance().loadImage(mediaObj,imageview, activity);
        }

        public void setOnClickListener(SingleClickListener listener) {
            main_cont.setOnClickListener(listener);
        }


        public void setSelected(int selectNumber) {
            cont_select_overlay.setVisibility(View.VISIBLE);
            tv_number.setText(""+selectNumber);
        }

        public void setUnSelected() {
            cont_select_overlay.setVisibility(View.GONE);
        }

        public void setImageOverlay(boolean set) {
            image_overlay.setVisibility(set ? View.VISIBLE :View.GONE);
        }
    }


}
