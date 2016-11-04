package com.prashantmaurice.android.mediapicker.Views;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.ExternalInterface.CaptureImgFolder;
import com.prashantmaurice.android.mediapicker.ExternalInterface.CustomFolder;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PicassoUtils;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;
import com.squareup.picasso.Picasso;

import static com.prashantmaurice.android.mediapicker.Utils.PicassoUtils.VideoThumbnailRequestHandler;

/**
 * Documented by maurice :
 *
 * This is basic builder for getting a open groups view which can be used to inflate in Explore open group pages
 */
public class FolderViewBuilder {
    private static final String TAG = "FOLDERVIEW";


    public static View getView(FolderActivity activity){
        LayoutInflater inflator = LayoutInflater.from(activity);
        View mainView = inflator.inflate(R.layout.adapterview_folder, null);
        ViewHolder holder = new ViewHolder(mainView, activity);
        mainView.setTag(holder);

        return mainView;
                
    }

    public static View getView(FolderActivity activity, MLocalFolderObj MLocalFolderObj){
        View view = getView(activity);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.inflateData(MLocalFolderObj);
        return view;
    }

    public static class ViewHolder{
        View mainView,main_cont,camera_icon;
        TextView tv_foldernum,tv_foldername;
        public ImageView imageview;

        FolderActivity activity;

        public ViewHolder(View itemView, FolderActivity activity) {
            this.activity = activity;
            mainView = itemView;
            main_cont =  itemView.findViewById(R.id.main_cont);
            camera_icon =  itemView.findViewById(R.id.camera_icon);

            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            tv_foldername = (TextView) itemView.findViewById(R.id.tv_foldername);
            tv_foldernum = (TextView) itemView.findViewById(R.id.tv_foldernum);
        }


        public void inflateData(final FolderObj folderObj){
            Logg.d(TAG, "Inflating data in FolderObj view : "+ folderObj.getName());
            camera_icon.setVisibility(View.GONE);
            tv_foldername.setText(folderObj.getName());
            if(folderObj instanceof MLocalFolderObj){
                MLocalFolderObj localFolderObj = (MLocalFolderObj) folderObj;
                MediaObj mediaObj = localFolderObj.getLatestMediaObj();
                tv_foldernum.setText("" + folderObj.getItemCount());
                switch (localFolderObj.getMediaType()){
                    case VIDEO:
                        activity.getPicassoForVideo()
                                .load(VideoThumbnailRequestHandler.SCHEME +"://"+mediaObj.getMediaId())
                                .into(imageview);

//                        Uri videoThumbUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.toString(mediaObj.getMediaId()));
//                        Picasso.with(activity)
//                                .load(videoThumbUri)
//                                .into(imageview);
                        break;


                    case IMAGE:

                        MImageObj imageObj = (MImageObj) mediaObj;
                        activity.getPicassoForImage()
                                .load(PicassoUtils.ImageThumbnailRequestHandler.getUriRequest(mediaObj.getMediaId(),imageObj.getOrientation()))
                                .into(imageview);


//                        Uri imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(mediaObj.getMediaId()));
//                        Picasso.with(activity)
//                                .load(imageURI)
//                                .into(imageview);
                        break;
                }



//
//                if (localFolderObj.getMediaType() == MediaPicker.Pick.VIDEO) {
//                    Uri imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(mediaObj.getMediaId()));
//                } else if (mediaObj.getMediaType() == MediaPicker.Pick.IMAGE) {
//                    Bitmap bitmapOrg = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), mediaObj.getMediaId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
//                    MImageObj mImageObj = (MImageObj) mediaObj;
//                    if (mImageObj.getOrientation() == 0) return bitmapOrg;
//                    Matrix matrix = new Matrix();
//                    matrix.postRotate(mImageObj.getOrientation());
//                    Bitmap bitmapRotated = Bitmap.createBitmap(bitmapOrg, 0, 0, bitmapOrg.getWidth(), bitmapOrg.getHeight(), matrix, true);
//                    if(bitmapOrg != null && !bitmapOrg.isRecycled()) bitmapOrg.recycle();
//                    return bitmapRotated;
//                } else {
//                    return null;
//                }

//                Uri imageURI = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Integer.toString(columnIndex));
//                Picasso
//                        .with(context)
//                        .load(imageURI)
//                        .fit()
//                        .centerInside()
//                        .into(imageView);
//
//                if(localFolderObj.getLatestMediaObj()!=null) Picasso.with(activity).load(localFolderObj.getLatestMediaObj().getMainUri()).into(imageview);

//                if(localFolderObj.getLatestMediaObj()!=null) BitmapLoaderController.getInstance().loadImage(localFolderObj.getLatestMediaObj(),imageview, activity);
            }else if(folderObj instanceof CustomFolder){
                CustomFolder customFolder = (CustomFolder) folderObj;
                Logg.d("CUSOTMMM"," : "+customFolder.getBGUri());
                Picasso.with(activity).load(customFolder.getBGUri()).into(imageview);
                tv_foldernum.setText("" + folderObj.getItemCount());
//                BitmapLoaderController.getInstance().loadImage(customFolder.getBGUri(),imageview, activity);
            }else if(folderObj instanceof CaptureImgFolder){
                CaptureImgFolder customFolder = (CaptureImgFolder) folderObj;
                imageview.setImageResource(R.drawable.empty);
                camera_icon.setVisibility(View.VISIBLE);
                tv_foldernum.setText("");
            }
        }

        public void setOnClickListener(SingleClickListener listener) {
            main_cont.setOnClickListener(listener);
        }
    }


}
