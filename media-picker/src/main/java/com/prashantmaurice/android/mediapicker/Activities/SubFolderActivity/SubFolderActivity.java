package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.ImageObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;

import java.util.ArrayList;
import java.util.List;

public class SubFolderActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    String TAG = "SUB_FOLDERACTIVITY";
    public static final int RESULT_BACKPRESSED = 400;
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
                getSupportLoaderManager().initLoader(1, null, SubFolderActivity.this);
            }

            @Override
            public void onRejected() {

            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media._ID};
        return new CursorLoader(this,u, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        List<ImageObj> images = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String fullName = c.getString(0);
            String tempDir = fullName.substring(0, fullName.lastIndexOf("/"));

            if(intentData.folderName.equals(tempDir)){
                ImageObj imageObj = new ImageObj(fullName);
                imageObj.id = c.getLong(1);
                images.add(imageObj);
            }

            c.moveToNext();
        }

        uiHandler.setData(images);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

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
