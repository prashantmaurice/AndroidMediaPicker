package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;

import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultDataBuilder;
import com.prashantmaurice.android.mediapicker.ExternalInterface.SelectionObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class MImageObj {

    private String path = "Dummy";
    private long id;


    public MImageObj(){}
    public MImageObj(String path){
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

    public static MImageObj decodeFromServer(JSONObject obj){
        MImageObj activityObject = new MImageObj();
        try {
            activityObject.path = (obj.has("path"))?obj.getString("path"):null;
            activityObject.id = (obj.has("id"))?obj.getLong("id"):0;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return activityObject;
    }


    public static ArrayList<MImageObj> decodeFromServer(JSONArray obj){
        ArrayList<MImageObj> list = new ArrayList<>();
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

    public static JSONArray encode(List<MImageObj> imagesList){
        JSONArray arr = new JSONArray();
        for(MImageObj MImageObj : imagesList) arr.put(MImageObj.encode());
        return arr;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof MImageObj){
            MImageObj c = (MImageObj) o;
            if (this.getId()==c.getId()) return true;
        }
        return false;
    }

    public static MImageObj initializeFromUri(Uri uri) {
        MImageObj MImageObj = new MImageObj();
        MImageObj.setPath(uri.getPath());
        return MImageObj;
    }

    public SelectionObject toImageObject(){
        return ResultDataBuilder.generateSelectionObject(this);
    }
}
