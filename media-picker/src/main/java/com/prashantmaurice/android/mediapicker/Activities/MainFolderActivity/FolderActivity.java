package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.Data.MainDataHandler;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;

import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {

    FolderActivityUIHandler uiHandler;
    PermissionController permissionController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        uiHandler =  new FolderActivityUIHandler(this);
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

            }else{

            }
        }
    }
}
