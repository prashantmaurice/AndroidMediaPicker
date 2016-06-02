package com.prashantmaurice.android.mediapicker.Utils;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.prashantmaurice.android.mediapicker.Utils.Constants.RequestCodes.PermissionController.RESUEST_PERMISSION;

/**
 * Created by maurice on 02/06/16.
 */
public class PermissionController {
    private final AppCompatActivity activity;

    static final int RESUEST_PERMISSION = 1002;


    private Map<String, ArrayList<TaskCallback>> pending = new HashMap<>();


    public interface TaskCallback{
        void onGranted();
        void onRejected();
    }

    public PermissionController(AppCompatActivity folderActivity){
        this.activity = folderActivity;
    }


    public void checkPermissionAndRun(String permission, TaskCallback taskCallback) {
        int permissionCheck = ContextCompat.checkSelfPermission(activity, permission);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RESUEST_PERMISSION);
            addPendingtask(permission,taskCallback);
        } else {
            //set some data
            taskCallback.onGranted();
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case RESUEST_PERMISSION:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    for (String permission : permissions) {
                        setGrantedPermission(permission);
                    }
                }else if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_DENIED)) {
                    for (String permission : permissions) {
                        setDeniedPermission(permission);
                    }
                }
                break;

            default:
                break;
        }
    }

    private void setGrantedPermission(String permission){
        ArrayList<TaskCallback> pemdingtasks = pending.get(permission);
        for(TaskCallback c : pemdingtasks) c.onGranted();
        pending.remove(permission);
    }

    private void setDeniedPermission(String permission){
        ArrayList<TaskCallback> pemdingtasks = pending.get(permission);
        for(TaskCallback c : pemdingtasks) c.onRejected();
        pending.remove(permission);
    }

    private void addPendingtask(String permission, TaskCallback cb){
        if(!pending.containsKey(permission)){
            pending.put(permission,new ArrayList<TaskCallback>());
        }

        pending.get(permission).add(cb);
    }
}
