package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
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
        viewHolder.btn_done =  folderActivity.findViewById(R.id.btn_done);
        viewHolder.gridView = (GridView) folderActivity.findViewById(R.id.gridView);
        viewHolder.actionbar_selected =  folderActivity.findViewById(R.id.actionbar_selected);
        viewHolder.actionbar_unselected =  folderActivity.findViewById(R.id.actionbar_unselected);
        viewHolder.tv_selected = (TextView) folderActivity.findViewById(R.id.tv_selected);
        viewHolder.tv_title = (TextView) folderActivity.findViewById(R.id.tv_title);
        viewHolder.btn_remove_selected =  folderActivity.findViewById(R.id.btn_remove_selected);



        initializeGridView(viewHolder.gridView);
        refreshActionbarState();
    }

    private void initializeGridView(GridView gridView) {
        adapter = new SubFolderActivityAdapter(folderActivity);
        gridView.setAdapter(adapter);
    }

    private void initializeListeners() {
        viewHolder.btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(SubFolderActivity.RESULT_BACKPRESSED);
            }
        });
        viewHolder.btn_remove_selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FolderActivity.getSelectionController().clearSelection();
                adapter.notifyDataSetChanged();
                refreshActionbarState();
            }
        });
        viewHolder.btn_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishActivity(SubFolderActivity.RESULT_OK);
            }
        });
    }

    private void finishActivity(int result){
        folderActivity.setResult(result);
        folderActivity.finish();
    }

    public void setTitle(String title){
        viewHolder.tv_title.setText(title);
    }
    public void setData(List<MediaObj> foldersList){
        adapter.setData(foldersList);
        adapter.notifyDataSetChanged();
    }

    public void refreshActionbarState(){
        int count = FolderActivity.getSelectionController().getSelectedMedias().size();
        int startCount = FolderActivity.getConfiguration().getStartFrom();

        if(count == 1 && !FolderActivity.getConfiguration().isSelectMultiple()){
            finishActivity(SubFolderActivity.RESULT_OK);
        } else {
            if (count > 0) {
                viewHolder.actionbar_selected.setVisibility(View.VISIBLE);
                viewHolder.actionbar_unselected.setVisibility(View.GONE);
                viewHolder.tv_selected.setText("" + (count + startCount) + " selected");
            } else {
                viewHolder.actionbar_selected.setVisibility(View.GONE);
                viewHolder.actionbar_unselected.setVisibility(View.VISIBLE);
            }
        }
    }


    //Main View Holder for all the views in this activity
    class ViewHolder{
        View btn_back, btn_remove_selected, btn_done;
        View actionbar_selected, actionbar_unselected;
        GridView gridView;
        TextView tv_selected, tv_title;
    }
}
