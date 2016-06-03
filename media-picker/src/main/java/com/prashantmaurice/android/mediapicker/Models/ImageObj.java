package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class ImageObj {

    private String path = "Dummy";
    private long id;


    public ImageObj(){}
    public ImageObj(String path){
        this.path = path;
    }

    public String getPath() {
        return ""+path;
    }
    public long getId() {
        return id;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setId(long id) {
        this.id = id;
    }

    public Uri getURI(){
        if(id!=0) return Uri.fromFile(new File("/document/image:"+id));
        return Uri.fromFile(new File(getPath()));
    }

    public static ImageObj decodeFromServer(JSONObject obj){
        ImageObj activityObject = new ImageObj();
        try {
            activityObject.path = (obj.has("path"))?obj.getString("path"):null;
            activityObject.id = (obj.has("id"))?obj.getLong("id"):0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activityObject;
    }


    public static ArrayList<ImageObj> decodeFromServer(JSONArray obj){
        ArrayList<ImageObj> list = new ArrayList<>();
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
            obj.put("path",path);
            obj.put("id",id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static JSONArray encode(List<ImageObj> imagesList){
        JSONArray arr = new JSONArray();
        for(ImageObj imageObj : imagesList) arr.put(imageObj.encode());
        return arr;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof ImageObj){
            ImageObj c = (ImageObj) o;
            if (this.getId()==c.getId()) return true;
        }
        return false;
    }

    public static ImageObj initializeFromUri(Uri uri) {
        ImageObj imageObj = new ImageObj();
        imageObj.setPath(uri.getPath());
        return imageObj;
    }
}
