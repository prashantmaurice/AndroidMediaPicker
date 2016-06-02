package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity.SubFolderActivity.RESULT_BACKPRESSED;

public class FolderActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    String TAG = "FOLDERACTIVITY";
    FolderActivityUIHandler uiHandler;
    PermissionController permissionController;
    SelectionController selectionController;
    List<FolderObj> folder = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logg.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        uiHandler =  new FolderActivityUIHandler(this);
        selectionController = SelectionController.getInstance();
        permissionController = new PermissionController(this);
        permissionController.checkPermissionAndRun(Manifest.permission.WRITE_EXTERNAL_STORAGE, new PermissionController.TaskCallback(){
            @Override
            public void onGranted() {
                getSupportLoaderManager().initLoader(1, null, FolderActivity.this);
            }

            @Override
            public void onRejected() {

            }
        });
    }

    private void setFolderData(List<FolderObj> folders) {
        List<FolderObj> list = new ArrayList<>();
        list.addAll(folders);
        uiHandler.setData(list);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logg.d(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.RequestCodes.FolderActivity.REQUEST_SUBFOLDER){
            switch (resultCode){
                case RESULT_OK:
                    MediaPicker.ResultParser.ResultData data2 = new MediaPicker.ResultParser.ResultData();
                    data2.setSelectedPics(selectionController.getSelectedPics());
                    setResult(RESULT_OK, data2.build());
                    finish();
                    break;
                case RESULT_CANCELED:
                    setResult(RESULT_CANCELED);
                    finish();
                    break;
                case RESULT_BACKPRESSED:
                    break;

            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        return new CursorLoader(this,u, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        Map<String, FolderObj> folders = new HashMap<>();

        c.moveToFirst();
        while (!c.isAfterLast()) {
            String tempDir = c.getString(0);
            tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));

            if(!folders.containsKey(tempDir)){
                folders.put(tempDir,new FolderObj(tempDir));
            }

            FolderObj folderObj = folders.get(tempDir);
            folderObj.setItemCount(folderObj.getItemCount()+1);

            c.moveToNext();
        }

        List<FolderObj> resultIAV = new ArrayList<>();
        Iterator<FolderObj> i = folders.values().iterator();
        FolderObj next;
        while(i.hasNext()){
            next = i.next();
            resultIAV.add(next);
        }

        setFolderData(resultIAV);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Logg.d(TAG,"onLoaderReset");
    }

}
