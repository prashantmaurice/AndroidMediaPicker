package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.TextView;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;

import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SubFolderActivityUIHandler {
    private final SubFolderActivity folderActivity;
    private final List<MediaObj> mediaArr;
    SubFolderActivityAdapter adapter;

    ViewHolder viewHolder = new ViewHolder();


    public SubFolderActivityUIHandler(SubFolderActivity folderActivity, List<MediaObj> mediaArr) {
        this.mediaArr = mediaArr;
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
        adapter = new SubFolderActivityAdapter(folderActivity, mediaArr);
        gridView.setAdapter(adapter);
        gridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(totalItemCount-firstVisibleItem-visibleItemCount<5){
                    folderActivity.scrolledToEnd();
                }
            }
        });
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
    public void notifyDataSetChanged(){
        adapter.notifyDataSetChanged();
    }

    public void refreshActionbarState(){
        int count = SelectionController.getInstance().getSelectedMedias().size();
        int startCount = FolderActivity.getConfiguration().getStartFrom();

        if(!checkForFinish()){
            if (count > 0) {
                viewHolder.actionbar_selected.setVisibility(View.VISIBLE);
                viewHolder.actionbar_unselected.setVisibility(View.GONE);
                viewHolder.tv_selected.setText("" + (count + startCount) + " selected");
            } else {
                viewHolder.actionbar_selected.setVisibility(View.GONE);
                viewHolder.actionbar_unselected.setVisibility(View.VISIBLE);
            }
        }else{
            finishActivity(SubFolderActivity.RESULT_OK);
        }

//        if(count == 1 && !FolderActivity.getConfiguration().isSelectMultiple()){
//            finishActivity(SubFolderActivity.RESULT_OK);
//        } else if(videocount == 1 && (!FolderActivity.getConfiguration().isSelectMultiple() || FolderActivity.getConfiguration().getMaximumVideoCount()==1)){
//            finishActivity(SubFolderActivity.RESULT_OK);
//        }else{
//            if (count > 0) {
//                viewHolder.actionbar_selected.setVisibility(View.VISIBLE);
//                viewHolder.actionbar_unselected.setVisibility(View.GONE);
//                viewHolder.tv_selected.setText("" + (count + startCount) + " selected");
//            } else {
//                viewHolder.actionbar_selected.setVisibility(View.GONE);
//                viewHolder.actionbar_unselected.setVisibility(View.VISIBLE);
//            }
//        }
    }

    //if we need to finish this module with result
    public static boolean checkForFinish(){
        int count = FolderActivity.getSelectionController().getSelectedMedias().size();
        int videocount = FolderActivity.getSelectionController().getSelectedVideoMedias().size();
        if(count == 1){
            if(!FolderActivity.getConfiguration().isSelectMultiple()){
                return true;
            }else if(FolderActivity.getConfiguration().getMaximumVideoCount()==1 && videocount == 1){
                return true;
            }
        }
        return false;
    }


    //Main View Holder for all the views in this activity
    class ViewHolder{
        View btn_back, btn_remove_selected, btn_done;
        View actionbar_selected, actionbar_unselected;
        GridView gridView;
        TextView tv_selected, tv_title;
    }
}
