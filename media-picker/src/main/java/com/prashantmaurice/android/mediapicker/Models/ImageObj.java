package com.prashantmaurice.android.mediapicker.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class ImageObj {

    private String path = "Dummy";
    public long id;


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




    public static ImageObj decodeFromServer(JSONObject obj){
        ImageObj activityObject = new ImageObj();
        try {
            activityObject.path = (obj.has("path"))?obj.getString("path"):null;
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
}
