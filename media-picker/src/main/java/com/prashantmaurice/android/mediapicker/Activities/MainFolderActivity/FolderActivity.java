package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.Data.MainDataHandler;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {

    FolderActivityUIHandler uiHandler;
    PermissionController permissionController;
    SelectionController selectionController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        uiHandler =  new FolderActivityUIHandler(this);
        selectionController = SelectionController.getInstance();
        permissionController = new PermissionController(this);
        permissionController.checkPermissionAndRun(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionController.TaskCallback(){
            @Override
            public void onGranted() {
                setFolderData();
            }

            @Override
            public void onRejected() {

            }
        });
    }

    private void setFolderData() {
        List<FolderObj> list = new ArrayList<>();
        list.addAll(MainDataHandler.getInstance().getAllDirectories(this));
        uiHandler.setData(list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.RequestCodes.FolderActivity.REQUEST_SUBFOLDER){
            if(resultCode == RESULT_OK){
                MediaPicker.ResultParser.ResultData data2 = new MediaPicker.ResultParser.ResultData();
                data2.setSelectedPics(selectionController.getSelectedPics());
                setResult(RESULT_OK, data2.build());
                finish();
            }else{
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    }
}
