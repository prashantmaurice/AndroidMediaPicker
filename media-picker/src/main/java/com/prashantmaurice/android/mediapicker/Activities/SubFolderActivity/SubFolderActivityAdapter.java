package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;
import com.prashantmaurice.android.mediapicker.Utils.ToastMain2;
import com.prashantmaurice.android.mediapicker.Views.ImageViewBuilder;

import java.util.List;

/**
 * Created by maurice on 01/09/15.
 */
public class SubFolderActivityAdapter extends BaseAdapter {
    private final List<MediaObj> mediaArr;
    private final SubFolderActivity activity;

    public SubFolderActivityAdapter(SubFolderActivity activity, List<MediaObj> mediaArr) {
        this.mediaArr = mediaArr;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return mediaArr.size();
    }

    @Override
    public MediaObj getItem(int position) {
        return mediaArr.get(position);
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
        final MediaObj mediaObj = mediaArr.get(position);
        holder.loadImage(mediaObj);

        long maxAllowedSize = FolderActivity.getConfiguration().getMaximumFileSize();
        final boolean maxReached = (maxAllowedSize > 0 && mediaObj.getSize() > 0 && maxAllowedSize < mediaObj.getSize());
        holder.setImageOverlay(maxReached);
        holder.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {

                if(maxReached){
                    ToastMain2.showSmarterToast(activity, "", "This file is too large to send");
                } else {
                    FolderActivity.getSelectionController().toggle(activity, mediaObj);
                    notifyDataSetChanged();
                    activity.refreshActionbarState();
                }
            }
        });
        if(FolderActivity.getSelectionController().isSelected(mediaObj) && !SubFolderActivityUIHandler.checkForFinish()){
            holder.setSelected(FolderActivity.getSelectionController().getSelectNumber(mediaObj));
        }else{
            holder.setUnSelected();
        }

        return view;
    }

//    public void setData(List<MediaObj> data) {
//        folders.clear();
//        mediaArr.addAll(data);
//    }


}
