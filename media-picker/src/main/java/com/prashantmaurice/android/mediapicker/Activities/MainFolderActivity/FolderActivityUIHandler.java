package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;

import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
class FolderActivityUIHandler {
    private final FolderActivity folderActivity;
//    FolderActivityAdapter adapter;

    private ViewHolder viewHolder = new ViewHolder();


    FolderActivityUIHandler(FolderActivity folderActivity) {
        this.folderActivity = folderActivity;
        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        viewHolder.btn_back = folderActivity.findViewById(R.id.btn_back);
        viewHolder.btn_camera = folderActivity.findViewById(R.id.btn_camera);
//        viewHolder.gridView = (GridView) folderActivity.findViewById(R.id.gridView);
        viewHolder.viewPager = (ViewPager) folderActivity.findViewById(R.id.viewPager);
        viewHolder.titleStrip = (PagerSlidingTabStrip) folderActivity.findViewById(R.id.tabs);
        viewHolder.btn_camera.setVisibility(View.GONE);
        initializeViewPager();


//        initializeGridView(viewHolder.gridView);
    }

    private void initializeViewPager() {

        viewHolder.viewPager.setAdapter(new FolderPagerAdapter(folderActivity.getSupportFragmentManager(),folderActivity));
        viewHolder.titleStrip.setViewPager(viewHolder.viewPager);
        viewHolder.titleStrip.setVisibility(FolderActivity.getConfiguration().getPick().equals(MediaPicker.Pick.IMAGE_VIDEO)||FolderActivity.getConfiguration().getPick().equals(MediaPicker.Pick.VIDEO_IMAGE)?View.VISIBLE:View.GONE);
    }

//    private void initializeGridView(GridView gridView) {
//        adapter = new FolderActivityAdapter(folderActivity);
//        gridView.setAdapter(adapter);
//    }

    private void initializeListeners() {
        viewHolder.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderActivity.setResult(Activity.RESULT_CANCELED);
                BitmapLoaderController.getInstance().flushCache();
                folderActivity.finish();
            }
        });
        viewHolder.btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderActivity.captureFromCamera();
            }
        });
    }

    public void setData(List<FolderObj> foldersList){
//        adapter.setData(foldersList);
//        adapter.notifyDataSetChanged();
    }


    //Main View Holder for all the views in this activity
    class ViewHolder{
        View btn_back, btn_camera;
        ViewPager viewPager;
        com.astuetz.PagerSlidingTabStrip titleStrip;
//        GridView gridView;
    }
}
