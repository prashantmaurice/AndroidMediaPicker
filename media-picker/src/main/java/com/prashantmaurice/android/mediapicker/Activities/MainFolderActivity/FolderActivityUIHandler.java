package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.GridView;

import com.astuetz.PagerSlidingTabStrip;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;

import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class FolderActivityUIHandler {
    private final FolderActivity folderActivity;
//    FolderActivityAdapter adapter;

    ViewHolder viewHolder = new ViewHolder();


    public FolderActivityUIHandler(FolderActivity folderActivity) {
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

        if(FolderActivity.getConfiguration().getFrom() == MediaPicker.From.GALLERY){
            viewHolder.btn_camera.setVisibility(View.GONE);
        } else {
            viewHolder.btn_camera.setVisibility(View.VISIBLE);
        }

        initializeViewPager();


//        initializeGridView(viewHolder.gridView);
    }

    private void initializeViewPager() {

        viewHolder.viewPager.setAdapter(new FolderPagerAdapter(folderActivity.getSupportFragmentManager(),folderActivity));
        viewHolder.titleStrip.setViewPager(viewHolder.viewPager);
        viewHolder.titleStrip.setVisibility(folderActivity.getConfiguration().getPick().equals(MediaPicker.Pick.IMAGE_VIDEO)?View.VISIBLE:View.GONE);
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
