package com.prashantmaurice.android.mediapicker.Views;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;

/**
 * Documented by maurice :
 *
 * This is basic builder for getting a open groups view which can be used to inflate in Explore open group pages
 */
public class FolderViewBuilder {
    static final String TAG = "FOLDERVIEW";


    public static View getView(Activity activity){
        LayoutInflater inflator = LayoutInflater.from(activity);
        View mainView = inflator.inflate(R.layout.adapterview_folder, null);
        ViewHolder holder = new ViewHolder(mainView, activity);
        mainView.setTag(holder);

        return mainView;
                
    }

    public static View getView(Activity activity, FolderObj folderObj){
        View view = getView(activity);
        ViewHolder holder = (ViewHolder) view.getTag();
        holder.inflateData(folderObj);
        return view;
    }

    public static class ViewHolder{
        public View mainView;
        public TextView tv_foldernum,tv_foldername;
        public ImageView imageview;

        Activity activity;

        public ViewHolder(View itemView, Activity activity) {
            this.activity = activity;
            mainView = itemView;
            imageview = (ImageView) itemView.findViewById(R.id.imageview);
            tv_foldername = (TextView) itemView.findViewById(R.id.tv_foldername);
            tv_foldernum = (TextView) itemView.findViewById(R.id.tv_foldernum);
        }




        public void inflateData(final FolderObj folderObj){
            Logg.d(TAG, "Inflating data in FolderObj view : "+folderObj.getName());
            tv_foldername.setText(folderObj.getName());
            tv_foldernum.setText("" + folderObj.getItemCount());
        }


        public void setOnClickListener(SingleClickListener listener) {
            mainView.setOnClickListener(listener);
        }
    }
}