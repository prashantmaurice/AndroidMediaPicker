package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.Data.MainDataHandler;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;

import java.util.ArrayList;
import java.util.List;

public class SubFolderActivity extends AppCompatActivity {

    SubFolderActivityUIHandler uiHandler;
    PermissionController permissionController;
    IntentBuilder.IntentData intentData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subfolder);

        intentData = IntentBuilder.parseResult(getIntent());
        uiHandler =  new SubFolderActivityUIHandler(this);
        permissionController = new PermissionController(this);
        permissionController.checkPermissionAndRun(Manifest.permission.READ_EXTERNAL_STORAGE, new PermissionController.TaskCallback(){
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
        List<ImageObj> list = new ArrayList<>();
        list.addAll(MainDataHandler.getInstance().getFromFolder(this, new FolderObj(intentData.folderName)));
        uiHandler.setData(list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }



    //Use this to build intent for calling this class
    public static class IntentBuilder{
        private static String INTENT_FOLDER_NAME = "folder-name";

        IntentData intentData = new IntentData();

        public IntentBuilder(){};

        public IntentBuilder setFolderObj(FolderObj folderObj) {
            intentData.folderName = folderObj.getName();
            return this;
        }

        public Intent build(Context context){
            Intent intent = new Intent(context, SubFolderActivity.class);
            intent.putExtra(INTENT_FOLDER_NAME, intentData.folderName);
            return intent;
        }

        //Use this to parse returned data
        public static IntentData parseResult(Intent data) {
            IntentData intentData = new IntentData();

            if(data.hasExtra(IntentBuilder.INTENT_FOLDER_NAME)){
                intentData.folderName = data.getStringExtra(IntentBuilder.INTENT_FOLDER_NAME);
            }


            return intentData;
        }

        //Main data object that is created with this builder
        public static class IntentData{
            public String folderName;
        }
    }


}
