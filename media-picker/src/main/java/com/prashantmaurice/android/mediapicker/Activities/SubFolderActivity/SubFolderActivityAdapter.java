package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;
import com.prashantmaurice.android.mediapicker.Utils.SingleClickListener;
import com.prashantmaurice.android.mediapicker.Views.ImageViewBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 01/09/15.
 */
public class SubFolderActivityAdapter extends BaseAdapter {
    private final List<MImageObj> folders = new ArrayList<>();
    private final SubFolderActivity activity;

    public SubFolderActivityAdapter(SubFolderActivity activity) {
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return folders.size();
    }

    @Override
    public MImageObj getItem(int position) {
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
        final MImageObj MImageObj = folders.get(position);
        holder.loadImage(MImageObj);
        holder.setOnClickListener(new SingleClickListener() {
            @Override
            public void onSingleClick(View view) {
                SelectionController.getInstance().toggle(MImageObj);
                notifyDataSetChanged();
                activity.refreshActionbarState();
            }
        });
        if(SelectionController.getInstance().isSelected(MImageObj)){
            holder.setSelected(SelectionController.getInstance().getSelectNumber(MImageObj));
        }else{
            holder.setUnSelected();
        }

        return view;
    }

    public void setData(List<MImageObj> data) {
        folders.clear();
        folders.addAll(data);
    }


}
