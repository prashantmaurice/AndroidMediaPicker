package com.prashantmaurice.android.mediapicker.Data;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by maurice on 02/06/16.
 */
public class MainDataHandler {
    static MainDataHandler instance;

    public static MainDataHandler getInstance(){
        if(instance==null) instance = new MainDataHandler();
        return instance;
    }

    MainDataHandler(){

    }

    public List<FolderObj> getAllDirectories(Activity activity){
        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.ImageColumns.DATA};
        Cursor c = null;
        List<FolderObj> resultIAV = new ArrayList<>();

        Map<String, FolderObj> folders = new HashMap<>();

        if (u != null){
            c = activity.managedQuery(u, projection, null, null, null);
        }

        if ((c != null) && (c.moveToFirst())){
            do{
                String tempDir = c.getString(0);
                tempDir = tempDir.substring(0, tempDir.lastIndexOf("/"));

                if(!folders.containsKey(tempDir)){
                    folders.put(tempDir,new FolderObj(tempDir));
                }

                FolderObj folderObj = folders.get(tempDir);
                folderObj.setItemCount(folderObj.getItemCount()+1);

            }while (c.moveToNext());

            c.close();


            Iterator<FolderObj> i = folders.values().iterator();
            FolderObj next;
            while(i.hasNext()){
                next = i.next();
                resultIAV.add(next);
            }
        }


        return resultIAV;
    }



}
