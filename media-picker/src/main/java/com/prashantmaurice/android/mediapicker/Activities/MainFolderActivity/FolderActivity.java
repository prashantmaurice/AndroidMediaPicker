package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.Manifest;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;
import com.prashantmaurice.android.mediapicker.ExternalInterface.CustomFolder;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultData;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultDataBuilder;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.Models.MVideoObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;
import com.prashantmaurice.android.mediapicker.Utils.PicassoUtils;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;
import com.prashantmaurice.android.mediapicker.Utils.ToastMain;
import com.prashantmaurice.android.mediapicker.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity.SubFolderActivity.RESULT_BACKPRESSED;

/**
 *
 *
 * This is the core Base Activity
 */
public class FolderActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    String TAG = "FOLDERACTIVITY";
    FolderActivityUIHandler uiHandler;
    PermissionController permissionController;
    Uri cameraURI;

    //Static variables
    static SelectionController selectionController;
    static Configuration configuration;

    static Picasso picassoForVideo;
    static Picasso picassoForImage;

    public static SelectionController getSelectionController() {
        return selectionController;
    }

    public static Configuration getConfiguration() {
        return configuration;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Logg.d(TAG,"onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);


        if(savedInstanceState==null){
            selectionController = SelectionController.getInstance();
            selectionController.reset();
            configuration = Configuration.parseResult(getIntent());
            if(configuration.getFrom().equals(MediaPicker.From.CAMERA)){
                captureFromCamera();
            }
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
        }else{
            if(savedInstanceState.getString(SAVE_CAMERA_URI)!=null){
                cameraURI = Uri.parse(savedInstanceState.getString(SAVE_CAMERA_URI));
            }
            selectionController = savedInstanceState.getParcelable(SAVE_SELECTION);
            configuration = savedInstanceState.getParcelable(SAVE_CONFIGURATION);

        }
        uiHandler =  new FolderActivityUIHandler(this);
    }

    private void setFolderData(List<FolderObj> folders) {
        List<FolderObj> list = new ArrayList<>();
        list.addAll(folders);
        uiHandler.setData(list);
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Logg.d(TAG,"onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.RequestCodes.FolderActivity.REQUEST_SUBFOLDER){
            switch (resultCode){
                case RESULT_OK:
                    ResultData data2 = ResultDataBuilder.getDataForPics(selectionController.getSelectedMedias());
                    setResult(RESULT_OK, ResultDataBuilder.toIntent(data2));
                    BitmapLoaderController.getInstance().flushCache();
                    finish();

                    break;
                case RESULT_CANCELED:
                    setResult(RESULT_CANCELED);
                    BitmapLoaderController.getInstance().flushCache();
                    finish();
                    break;
                case RESULT_BACKPRESSED:
                    break;
            }
        }

        if(requestCode == Constants.RequestCodes.FolderActivity.REQUEST_CAMERA){
            switch (resultCode){
                case RESULT_OK:
                    if(cameraURI!=null){
                        List<MediaObj> list = new ArrayList<>();
                        list.add(MImageObj.Builder.generateFromCameraResult(cameraURI));
                        list.addAll(selectionController.getSelectedMedias());
                        ResultData data2 = ResultDataBuilder.getDataForPics(list);
                        setResult(RESULT_OK, ResultDataBuilder.toIntent(data2));
                        BitmapLoaderController.getInstance().flushCache();
                        finish();
                    }else{
                        ToastMain.showSmarterToast(this,"CameraUri is empty",null);
                    }
                    break;
                case RESULT_CANCELED:
                    if(configuration.getFrom().equals(MediaPicker.From.CAMERA)){
                        finish();
                    }
                    break;
            }
        }
    }

    public void finishWithCustomFolderSelected(String folderId){
        ResultData data2 = ResultDataBuilder.getForCustomFolderSelectedAndMediaSelected(folderId,selectionController.getSelectedMedias());
        setResult(RESULT_OK, ResultDataBuilder.toIntent(data2));
        BitmapLoaderController.getInstance().flushCache();
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");

        if( configuration.getPick().equals(MediaPicker.Pick.VIDEO)) {
            Uri u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.VideoColumns.LATITUDE,
                    MediaStore.Video.VideoColumns.WIDTH,
                    MediaStore.Video.VideoColumns.HEIGHT,
                    MediaStore.Video.VideoColumns.LONGITUDE,//Orientation is always giving 0
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.VideoColumns.DESCRIPTION,
                    MediaStore.Video.VideoColumns.DATE_TAKEN
            };
            return new CursorLoader(this, u, projection, null, null, MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");
        }else { //(configuration.getPick() == MediaPicker.Pick.IMAGE) {
            Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.LATITUDE,
                    MediaStore.Images.ImageColumns.WIDTH,
                    MediaStore.Images.ImageColumns.HEIGHT,
                    MediaStore.Images.ImageColumns.LONGITUDE,//Orientation is always giving 0
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.ImageColumns.DESCRIPTION,
                    MediaStore.Images.ImageColumns.DATE_TAKEN
            };
            return new CursorLoader(this, u, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        }
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        Map<String, MLocalFolderObj> folders = new HashMap<>();

        c.moveToFirst();


        if(configuration.getPick().equals(MediaPicker.Pick.VIDEO)){
            while (!c.isAfterLast()) {
                String fullPath = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DATA));// /storage/emulated/0/TinyStep/TinyStep Images/IMG 1439401529025.jpg
                if(fullPath==null) fullPath = "";
                String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

                if (!folders.containsKey(tempDir)) {
                    MLocalFolderObj mLocalFolderObj = new MLocalFolderObj(tempDir, configuration.getPick());

                    //Set am imageObj as latest one
                    long id = c.getLong(c.getColumnIndex(MediaStore.Video.Media._ID));
                    long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                    double lat = c.getDouble(c.getColumnIndex(MediaStore.Video.Media.LATITUDE));
                    double longg = c.getDouble(c.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
                    int duration = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                    int width = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH));
                    int height = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT));
                    String desc = c.getString(c.getColumnIndex(MediaStore.Video.Media.DESCRIPTION));
                    MVideoObj mVideoObj = MVideoObj.Builder.generateFromMediaVideoCursor(id, dateTaken, width, height, lat, longg, desc, duration, 0);

                    mLocalFolderObj.setLatestMediaObj(mVideoObj);


                    folders.put(tempDir, mLocalFolderObj);
                }

                MLocalFolderObj MLocalFolderObj = folders.get(tempDir);
                MLocalFolderObj.setItemCount(MLocalFolderObj.getItemCount() + 1);
                c.moveToNext();
            }
        } else {
            while (!c.isAfterLast()) {
                String fullPath = c.getString(c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));// /storage/emulated/0/TinyStep/TinyStep Images/IMG 1439401529025.jpg
                if(fullPath==null) fullPath = "";
                String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

                if (!folders.containsKey(tempDir)) {
                    MLocalFolderObj mLocalFolderObj = new MLocalFolderObj(tempDir, configuration.getPick());

                    //Set am imageObj as latest one//1453763584//79870665600000 //
                    long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                    long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                    double lat = c.getDouble(c.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                    double longg = c.getDouble(c.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                    int orientation = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                    int width = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                    int height = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                    String desc = c.getString(c.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));

                    //parse date-taken as in few phones it contained 79870665600000,
                    if(dateTaken > System.currentTimeMillis()+(1000*60*60*24*1000)){
                        dateTaken = (new File(fullPath).lastModified());
                        if(dateTaken<10) dateTaken = System.currentTimeMillis();
                    }

                    MImageObj mImageObj = MImageObj.Builder.generateFromMediaImageCursor(id, dateTaken, width, height, lat, longg, desc, orientation, 0);
                    mLocalFolderObj.setLatestMediaObj(mImageObj);


                    folders.put(tempDir, mLocalFolderObj);
                }

                MLocalFolderObj MLocalFolderObj = folders.get(tempDir);
                MLocalFolderObj.setItemCount(MLocalFolderObj.getItemCount() + 1);
                c.moveToNext();
            }
        }

        List<FolderObj> resultIAV = new ArrayList<>();
        Iterator<MLocalFolderObj> i = folders.values().iterator();
        MLocalFolderObj next;
        while(i.hasNext()){
            next = i.next();
            resultIAV.add(next);
        }

        //add custom folders
        for(CustomFolder customFolder : configuration.getCustomFolders()){
            resultIAV.add(customFolder);
        }

        setFolderData(resultIAV);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Logg.d(TAG,"onLoaderReset");
    }


    static final String SAVE_CAMERA_URI = "camera_uri";
    static final String SAVE_SELECTION = "singleton";
    static final String SAVE_CONFIGURATION = "config";
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save data if activity is killed by camera/gallery
        if(cameraURI!=null) savedInstanceState.putString(SAVE_CAMERA_URI, cameraURI.toString());
        savedInstanceState.putParcelable(SAVE_SELECTION, selectionController);
        savedInstanceState.putParcelable(SAVE_CONFIGURATION,configuration);
        super.onSaveInstanceState(savedInstanceState);// Always call the superclass so it can save the view hierarchy state
    }

    public void captureFromCamera() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraURI = Utils.FileStorage.getOutputMediaFileUri();
        if(cameraURI ==null){
            ToastMain.showSmarterToast(this,null,"Error storing image");return;
        }
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraURI);
        startActivityForResult(cameraIntent, Constants.RequestCodes.FolderActivity.REQUEST_CAMERA);
    }

    public Picasso getPicassoForVideo(){
        if(picassoForVideo==null){
            picassoForVideo = new Picasso.Builder(this)
                    .addRequestHandler(new PicassoUtils.VideoThumbnailRequestHandler(this))
                    .build();
        }
        return picassoForVideo;
    }
    public Picasso getPicassoForImage(){
        if(picassoForImage==null){
            picassoForImage = new Picasso.Builder(this)
                    .addRequestHandler(new PicassoUtils.ImageThumbnailRequestHandler(this))
                    .build();
        }
        return picassoForImage;
    }
}
