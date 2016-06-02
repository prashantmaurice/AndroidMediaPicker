package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.app.Activity;
import android.view.View;
import android.widget.GridView;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;

import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class FolderActivityUIHandler {
    private final FolderActivity folderActivity;
    FolderActivityAdapter adapter;

    ViewHolder viewHolder = new ViewHolder();


    public FolderActivityUIHandler(FolderActivity folderActivity) {
        this.folderActivity = folderActivity;
        initializeViews();
        initializeListeners();
    }

    private void initializeViews() {
        viewHolder.btn_back = folderActivity.findViewById(R.id.btn_back);
        viewHolder.gridView = (GridView) folderActivity.findViewById(R.id.gridView);

        initializeGridView(viewHolder.gridView);
    }

    private void initializeGridView(GridView gridView) {
        adapter = new FolderActivityAdapter(folderActivity);
        gridView.setAdapter(adapter);
    }

    private void initializeListeners() {
        viewHolder.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderActivity.setResult(Activity.RESULT_CANCELED);
                folderActivity.finish();
            }
        });
    }

    public void setData(List<FolderObj> foldersList){
        adapter.setData(foldersList);
        adapter.notifyDataSetChanged();
    }


    //Main View Holder for all the views in this activity
    class ViewHolder{
        View btn_back;
        GridView gridView;
    }
}