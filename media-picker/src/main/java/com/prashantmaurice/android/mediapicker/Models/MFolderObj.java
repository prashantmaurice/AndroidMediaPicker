package com.prashantmaurice.android.mediapicker.Models;

import com.prashantmaurice.android.mediapicker.MediaPicker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by maurice on 02/06/16.
 */
public class MFolderObj {

    private String directory = "Dummy";
    private int itemCount = 0;
    private MediaObj latestMediaObj;
    private MediaPicker.Pick mediaType;


    public MFolderObj(){}
    public MFolderObj(String directory, MediaPicker.Pick mediaType){
        this.directory = directory;
        this.mediaType = mediaType;
    }

    public String getName() {
        if(!directory.contains("/")){
            return directory;
        }else{
            return directory.substring(directory.lastIndexOf("/")+1, directory.length());
        }
    }

    public String getPath() {
        return ""+directory;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public static MFolderObj decodeFromServer(JSONObject obj){
        MFolderObj activityObject = new MFolderObj();
        try {
            activityObject.directory = (obj.has("directory"))?obj.getString("directory"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activityObject;
    }


    public static ArrayList<MFolderObj> decodeFromServer(JSONArray obj){
        ArrayList<MFolderObj> list = new ArrayList<>();
        for(int i=0;i<obj.length();i++){
            try {
                list.add(decodeFromServer(obj.getJSONObject(i)));
            } catch (JSONException e) {e.printStackTrace();}
        }
        return list;
    }

    public JSONObject encode(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("directory",directory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public void setLatestMediaObj(MediaObj latestMVideoObj) {
        this.latestMediaObj = latestMVideoObj;
    }

    public MediaObj getLatestMediaObj() {
        return latestMediaObj;
    }

    public MediaPicker.Pick getMediaType(){
        return mediaType;
    }
}
