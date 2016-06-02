package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.widget.GridView;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;

import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SubFolderActivityUIHandler {
    private final SubFolderActivity folderActivity;
    SubFolderActivityAdapter adapter;

    ViewHolder viewHolder = new ViewHolder();


    public SubFolderActivityUIHandler(SubFolderActivity folderActivity) {
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
        adapter = new SubFolderActivityAdapter(folderActivity);
        gridView.setAdapter(adapter);
    }

    private void initializeListeners() {
        viewHolder.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                folderActivity.setResult(SubFolderActivity.RESULT_BACKPRESSED);
                folderActivity.finish();
            }
        });
    }

    public void setData(List<ImageObj> foldersList){
        adapter.setData(foldersList);
        adapter.notifyDataSetChanged();
    }


    //Main View Holder for all the views in this activity
    class ViewHolder{
        View btn_back;
        GridView gridView;
    }
}
