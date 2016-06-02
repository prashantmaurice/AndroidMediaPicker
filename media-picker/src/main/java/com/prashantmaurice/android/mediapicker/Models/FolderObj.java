package com.prashantmaurice.android.mediapicker.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by maurice on 02/06/16.
 */
public class FolderObj {

    private String directory = "Dummy";
    private int itemCount = 0;
    private ImageObj latestImageObj;


    public FolderObj(){}
    public FolderObj(String directory){
        this.directory = directory;
    }

    public String getName() {
        return ""+directory;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public static FolderObj decodeFromServer(JSONObject obj){
        FolderObj activityObject = new FolderObj();
        try {
            activityObject.directory = (obj.has("directory"))?obj.getString("directory"):null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activityObject;
    }


    public static ArrayList<FolderObj> decodeFromServer(JSONArray obj){
        ArrayList<FolderObj> list = new ArrayList<>();
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

    public void setLatestImageObj(ImageObj latestImageObj) {
        this.latestImageObj = latestImageObj;
    }

    public ImageObj getLatestImgObj() {
        return latestImageObj;
    }
}
