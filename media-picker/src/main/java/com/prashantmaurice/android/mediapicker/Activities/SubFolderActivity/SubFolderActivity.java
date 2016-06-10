package com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.Models.MFolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
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
        uiHandler.setTitle(intentData.folderName);
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
    public void onBackPressed() {
        setResult(SubFolderActivity.RESULT_BACKPRESSED);
        finish();
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
//    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA,MediaStore.Images.Media._ID};
        return new CursorLoader(this,u, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN+" DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        List<MImageObj> images = new ArrayList<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String fullName = c.getString(0);
            String tempDir = fullName.substring(0, fullName.lastIndexOf("/"));

            if(intentData.folderPath.equals(tempDir)){
                long id = c.getLong(1);
                MImageObj mImageObj = MImageObj.Builder.generateFromMediaImageCursor(id);
                images.add(mImageObj);
            }

            c.moveToNext();
        }

        uiHandler.setData(images);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void refreshActionbarState() {
        uiHandler.refreshActionbarState();
    }


    //Use this to build intent for calling this class
    public static class IntentBuilder{
        private static String INTENT_FOLDER_NAME = "folder-name";
        private static String INTENT_FOLDER_PATH = "folder-path";

        IntentData intentData = new IntentData();

        public IntentBuilder(){};

        public IntentBuilder setFolderObj(MFolderObj MFolderObj) {
            intentData.folderName = MFolderObj.getName();
            intentData.folderPath = MFolderObj.getPath();
            return this;
        }

        public Intent build(Context context){
            Intent intent = new Intent(context, SubFolderActivity.class);
            intent.putExtra(INTENT_FOLDER_NAME, intentData.folderName);
            intent.putExtra(INTENT_FOLDER_PATH, intentData.folderPath);
            return intent;
        }

        //Use this to parse returned data
        public static IntentData parseResult(Intent data) {
            IntentData intentData = new IntentData();

            if(data.hasExtra(IntentBuilder.INTENT_FOLDER_NAME)){
                intentData.folderName = data.getStringExtra(IntentBuilder.INTENT_FOLDER_NAME);
            }
            if(data.hasExtra(IntentBuilder.INTENT_FOLDER_PATH)){
                intentData.folderPath = data.getStringExtra(IntentBuilder.INTENT_FOLDER_PATH);
            }

            return intentData;
        }

        //Main data object that is created with this builder
        public static class IntentData{
            public String folderName;
            public String folderPath;
        }
    }


}
