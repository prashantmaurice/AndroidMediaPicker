package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Views.FolderViewBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 01/09/15.
 */
public class SubFolderActivityAdapter extends BaseAdapter {

    private final List<FolderObj> folders = new ArrayList<>();
    private final SubFolderActivity activity;

    public SubFolderActivityAdapter(SubFolderActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public FolderObj getItem(int position) {
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
            view = FolderViewBuilder.getView(activity);
        }
        else {
            view = convertView;
        }

        FolderViewBuilder.ViewHolder holder = (FolderViewBuilder.ViewHolder) view.getTag();


        //set view
        final FolderObj group = folders.get(position);
        holder.inflateData(group);

        return view;
    }

    public void setData(List<FolderObj> data) {
        folders.clear();
        folders.addAll(data);
    }
}
