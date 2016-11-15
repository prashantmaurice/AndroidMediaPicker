package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultData;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultDataBuilder;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.BitmapLoaderController;
import com.prashantmaurice.android.mediapicker.Utils.Constants;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.PermissionController;
import com.prashantmaurice.android.mediapicker.Utils.PicassoUtils;
import com.prashantmaurice.android.mediapicker.Utils.SelectionController;
import com.prashantmaurice.android.mediapicker.Utils.ToastMain2;
import com.prashantmaurice.android.mediapicker.Utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.prashantmaurice.android.mediapicker.Activities.SubFolderActivity.SubFolderActivity.RESULT_BACKPRESSED;

/**
 *
 *
 * This is the core Base Activity
 */
public class FolderActivity extends AppCompatActivity {
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
                        ToastMain2.showSmarterToast(this,"CameraUri is empty",null);
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
            ToastMain2.showSmarterToast(this,null,"Error storing image");return;
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
