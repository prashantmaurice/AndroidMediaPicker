package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.Views.ImageViewBuilder;

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
        Bitmap bitmap = MediaStore.Images.Thumbnails.getThumbnail(activity.getContentResolver(), imageObj.getId(), MediaStore.Images.Thumbnails.MICRO_KIND, null);
        holder.setBitmap(bitmap);

        return view;
    }

    public void setData(List<ImageObj> data) {
        folders.clear();
        folders.addAll(data);
    }
}
