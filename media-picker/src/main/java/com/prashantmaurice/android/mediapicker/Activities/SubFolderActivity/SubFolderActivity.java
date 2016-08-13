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

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MVideoObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;
import com.prashantmaurice.android.mediapicker.Utils.PicassoUtils;
import com.squareup.picasso.Cache;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SubFolderActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    String TAG = "SUB_FOLDERACTIVITY";
    public static final int RESULT_BACKPRESSED = 400;
    SubFolderActivityUIHandler uiHandler;
    PermissionController permissionController;
    IntentBuilder.IntentData intentData;
    static Picasso picassoForVideo;
    static Picasso picassoForImage;
    LruCache cache2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subfolder);
        intentData = IntentBuilder.parseResult(getIntent());
        cache2 = new LruCache(this);
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
        if(isInSelectionMode()){
            FolderActivity.getSelectionController().clearSelection();
            uiHandler.adapter.notifyDataSetChanged();
            uiHandler.refreshActionbarState();
        }else{
            setResult(SubFolderActivity.RESULT_BACKPRESSED);
            finish();
        }
    }

    //    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        permissionController.onRequestPermissionsResult(requestCode,permissions,grantResults);
//    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cache2.clear();
        if(picassoForImage!=null) picassoForImage.shutdown();
        if(picassoForVideo!=null) picassoForVideo.shutdown();
        picassoForImage = null;
        picassoForVideo = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cache2.clear();
        if(picassoForImage!=null) picassoForImage.shutdown();
        if(picassoForVideo!=null) picassoForVideo.shutdown();
        picassoForImage = null;
        picassoForVideo = null;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");

        if(intentData.mediaType.equals(MediaPicker.Pick.VIDEO)){
            Uri u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Video.VideoColumns.DATA,
                    MediaStore.Video.Media._ID,
                    MediaStore.Video.VideoColumns.LATITUDE,
                    MediaStore.Video.VideoColumns.LONGITUDE,
                    MediaStore.Video.VideoColumns.DURATION,
                    MediaStore.Video.VideoColumns.DESCRIPTION,
                    MediaStore.Video.VideoColumns.WIDTH,
                    MediaStore.Video.VideoColumns.HEIGHT,
                    MediaStore.Video.VideoColumns.DATE_TAKEN,
                    MediaStore.Video.Media.SIZE
            };
            return new CursorLoader(this, u, projection, null, null, MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");
        } else {

            Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {
                    MediaStore.Images.ImageColumns.DATA,
                    MediaStore.Images.Media._ID,
                    MediaStore.Images.ImageColumns.LATITUDE,
                    MediaStore.Images.ImageColumns.LONGITUDE,
                    MediaStore.Images.ImageColumns.ORIENTATION,
                    MediaStore.Images.ImageColumns.DESCRIPTION,
                    MediaStore.Images.ImageColumns.WIDTH,
                    MediaStore.Images.ImageColumns.HEIGHT,
                    MediaStore.Images.ImageColumns.DATE_TAKEN,
                    MediaStore.Images.Media.SIZE
            };
            return new CursorLoader(this, u, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        List<MediaObj> mediaObjs = new ArrayList<>();

        c.moveToFirst();

        if(intentData.mediaType == MediaPicker.Pick.VIDEO) {
            while (!c.isAfterLast()) {
                String fullPath = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

                if (intentData.folderPath.equals(tempDir)) {
                    //Set am imageObj as latest one
                    long id = c.getLong(c.getColumnIndex(MediaStore.Video.Media._ID));
                    long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Video.VideoColumns.DATE_TAKEN));
                    int width = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH));
                    int height = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT));
                    int duration = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                    double lat = c.getDouble(c.getColumnIndex(MediaStore.Video.VideoColumns.LATITUDE));
                    double longg = c.getDouble(c.getColumnIndex(MediaStore.Video.VideoColumns.LONGITUDE));
                    String desc = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DESCRIPTION));
                    long size = c.getLong(c.getColumnIndex(MediaStore.Video.Media.SIZE));

                    MVideoObj mVideoObj = MVideoObj.Builder.generateFromMediaVideoCursor(id, dateTaken, width, height, lat, longg, desc, duration, size);
                    mediaObjs.add(mVideoObj);
                }

                c.moveToNext();
            }
        } else {
            while (!c.isAfterLast()) {
                String fullPath = c.getString(c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

                if (intentData.folderPath.equals(tempDir)) {
                    //Set am imageObj as latest one
                    long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                    long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN));
                    int width = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                    int height = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                    int orientation = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                    double lat = c.getDouble(c.getColumnIndex(MediaStore.Images.ImageColumns.LATITUDE));
                    double longg = c.getDouble(c.getColumnIndex(MediaStore.Images.ImageColumns.LONGITUDE));
                    String desc = c.getString(c.getColumnIndex(MediaStore.Images.ImageColumns.DESCRIPTION));
                    long size = c.getLong(c.getColumnIndex(MediaStore.Images.Media.SIZE));

                    //parse date-taken as in few phones it contained 79870665600000,
                    if(dateTaken> System.currentTimeMillis()+(1000*60*60*24*1000)){
                        dateTaken = (new File(fullPath).lastModified());
                        if(dateTaken<10) dateTaken = System.currentTimeMillis();
                    }

                    MImageObj mImageObj = MImageObj.Builder.generateFromMediaImageCursor(id, dateTaken, width, height, lat, longg, desc, orientation, size);
                    mediaObjs.add(mImageObj);


                }

                c.moveToNext();
            }
        }

        uiHandler.setData(mediaObjs);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {}

    private boolean isInSelectionMode(){
        return FolderActivity.getSelectionController().getSelectedMedias().size()>0;
    }

    public Picasso getPicassoForVideo(){
        if(picassoForVideo==null){
            picassoForVideo = new Picasso.Builder(this)
                    .addRequestHandler(new PicassoUtils.VideoThumbnailRequestHandler(this))
                    .memoryCache(cache2)
                    .build();
        }
        return picassoForVideo;
    }
    public Picasso getPicassoForImage(){
        if(picassoForImage==null){
//            picassoForImage.cache.clear();;


            picassoForImage = new Picasso.Builder(this)
                    .addRequestHandler(new PicassoUtils.ImageThumbnailRequestHandler(this))
                    .memoryCache(cache2)
                    .build();
        }
        return picassoForImage;
    }

    public void refreshActionbarState() {
        uiHandler.refreshActionbarState();
    }

    //Use this to build intent for calling this class
    public static class IntentBuilder{
        private static String INTENT_FOLDER_NAME = "folder-name";
        private static String INTENT_FOLDER_PATH = "folder-path";
        private static String INTENT_MEDIA_TYPE = "media-type";

        IntentData intentData = new IntentData();

        public IntentBuilder(){};

        public IntentBuilder setFolderObj(MLocalFolderObj MLocalFolderObj) {
            intentData.folderName = MLocalFolderObj.getName();
            intentData.folderPath = MLocalFolderObj.getPath();
            intentData.mediaType = MLocalFolderObj.getMediaType();
            return this;
        }

        public Intent build(Context context){
            Intent intent = new Intent(context, SubFolderActivity.class);
            intent.putExtra(INTENT_FOLDER_NAME, intentData.folderName);
            intent.putExtra(INTENT_FOLDER_PATH, intentData.folderPath);
            intent.putExtra(INTENT_MEDIA_TYPE, intentData.mediaType.getStr());

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
            if(data.hasExtra(IntentBuilder.INTENT_MEDIA_TYPE)){
                String pickStr = data.getStringExtra(IntentBuilder.INTENT_MEDIA_TYPE);
                intentData.mediaType = MediaPicker.Pick.getPickForPickStr(pickStr);
            }

            return intentData;
        }

        //Main data object that is created with this builder
        public static class IntentData{
            public String folderName;
            public String folderPath;
            public MediaPicker.Pick mediaType;
        }
    }


}
