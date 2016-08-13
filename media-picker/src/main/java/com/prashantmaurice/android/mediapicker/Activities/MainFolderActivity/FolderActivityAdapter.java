package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity.SubFolderActivity;
import com.prashantmaurice.android.mediapicker.ExternalInterface.CustomFolder;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;
import com.prashantmaurice.android.mediapicker.Views.FolderViewBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 01/09/15.
 */
public class FolderActivityAdapter extends BaseAdapter {

    private final List<FolderObj> folders = new ArrayList<>();
    private final FolderActivity activity;

    public FolderActivityAdapter(FolderActivity activity) {
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
        holder.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                if(group instanceof MLocalFolderObj){
                    Intent intent = new SubFolderActivity.IntentBuilder()
                            .setFolderObj((MLocalFolderObj) group)
                            .build(activity);
                    activity.startActivityForResult(intent, Constants.RequestCodes.FolderActivity.REQUEST_SUBFOLDER);
                }else if(group instanceof CustomFolder){
                    activity.finishWithCustomFolderSelected(((CustomFolder)group).getCustomFolderId());
                }
            }
        });

        return view;
    }

    public void setData(List<FolderObj> data) {
        folders.clear();
        folders.addAll(data);
        notifyDataSetChanged();
    }
}
