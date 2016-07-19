package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.support.annotation.Nullable;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 19/07/16.
 */
public class CustomFolder extends FolderObj {
    String name;
    Integer itemCount;
    String backgroundUri;
    String customFolderId;

    public static CustomFolder decode(JSONObject obj){
        CustomFolder activityObject = new CustomFolder();
        try {
            activityObject.name = (obj.has("name"))?obj.getString("name"):"";
            activityObject.itemCount = (obj.has("itemCount"))?obj.getInt("itemCount"):null;
            activityObject.backgroundUri = (obj.has("backgroundUri"))?obj.getString("backgroundUri"):null;
            activityObject.customFolderId = (obj.has("customFolderId"))?obj.getString("customFolderId"):null;
        } catch (JSONException e) {
            e.printStackTrace(); throw new RuntimeException(e);
        }
        return activityObject;
    }

    public static List<CustomFolder> decode(JSONArray obj){
        ArrayList<CustomFolder> list = new ArrayList<>();
        for(int i=0;i<obj.length();i++){
            try {
                list.add(decode(obj.getJSONObject(i)));
            } catch (JSONException e) {e.printStackTrace();}
        }
        return list;
    }

    public static JSONArray encode(List<CustomFolder> resultData){
//        JSONObject obj = new JSONObject();
        JSONArray objects = new JSONArray();
//        try {
            for(CustomFolder selection : resultData) objects.put(selection.encode());
//            obj.put("objects",objects);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return objects;
    }
    private JSONObject encode(){
        JSONObject obj = new JSONObject();
        try {
            obj.put("name",name);
            obj.put("itemCount",itemCount);
            obj.put("backgroundUri",backgroundUri);
            obj.put("customFolderId",customFolderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getBGUri() {
        return backgroundUri;
    }


    @Override
    public int getItemCount() {
        return itemCount;
    }

    public String getCustomFolderId() {
        return customFolderId;
    }

    public static class Builder{
        public static CustomFolder generate(String name, @Nullable Integer itemCount, String backgroundUri, String customFolderId){
            CustomFolder customFolder = new CustomFolder();
            customFolder.name = name;
            customFolder.itemCount = itemCount;
            customFolder.backgroundUri = backgroundUri;
            customFolder.customFolderId = customFolderId;
            return customFolder;
        }
    }
}
