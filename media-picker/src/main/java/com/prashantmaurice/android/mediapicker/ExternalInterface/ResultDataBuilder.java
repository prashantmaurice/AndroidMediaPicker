package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 03/06/16.
 */
public class ResultDataBuilder {
    private static String INTENT_SELECTED = "selected";
    public static ResultData getDataForPics(List<MImageObj> images){
        ResultData data = new ResultData();
        data.selectedObjs.clear();
        data.selectedObjs.addAll(generateSelectionObject(images));
        return data;
    }
    public static Intent toIntent(ResultData data){
        Intent intent = new Intent();
        intent.putExtra(INTENT_SELECTED, Encoders.encode(data).toString());
        return intent;
    }
    public static ResultData parseResult(Intent data) {
        if(data.hasExtra(INTENT_SELECTED)){
            String jsonArrStr =  data.getStringExtra(INTENT_SELECTED);
            try {
                JSONObject jsonArr = new JSONObject(jsonArrStr);
                return Encoders.decode(jsonArr);
            } catch (JSONException e) {e.printStackTrace();}
        }
        return null;
    }
    public static List<SelectedMedia> generateSelectionObject(List<MImageObj> objArr){
        List<SelectedMedia> list = new ArrayList<>();
        for(MImageObj obj : objArr) list.add(generateSelectionObject(obj));
        return list;
    }

    public static SelectedMedia generateSelectionObject(MImageObj obj){
        SelectedMedia selection = new SelectedMedia();
        selection.type = obj.getType();
        selection.imageId = obj.getImageId();
        selection.datetaken = obj.getDateTaken();
        selection.latitude = obj.getLatitude();
        selection.longitude = obj.getLongitude();
        selection.width = obj.getWidth();
        selection.height = obj.getHeight();
        selection.desc = obj.getDesc();
        selection.uri = obj.getMainUri();
        return selection;
    }

    static class Encoders{
        public static ResultData decode(JSONObject obj){
            ResultData activityObject = new ResultData();
            try {
                JSONArray arr = obj.getJSONArray("objects");
                ArrayList<SelectedMedia> objects = new ArrayList<>();
                for(int i=0;i<arr.length();i++){
                    SelectedMedia ob2j = decodeSelectionObject(arr.getJSONObject(i));
                    if(ob2j!=null) objects.add(ob2j);
                }
                activityObject.selectedObjs = objects;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return activityObject;
        }

        public static JSONObject encode(ResultData resultData){
            JSONObject obj = new JSONObject();
            try {
                JSONArray objects = new JSONArray();
                for(SelectedMedia selection : resultData.getSelectedPics()) objects.put(encodeSelectionObject(selection));
                obj.put("objects",objects);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }
        private static JSONObject encodeSelectionObject(SelectedMedia selection){
            JSONObject obj = new JSONObject();
            try {
                obj.put("type",selection.getType().getString());
                obj.put("imageId",selection.imageId);
                obj.put("datetaken",selection.datetaken);
                obj.put("latitude",selection.latitude);
                obj.put("longitude",selection.longitude);
                obj.put("desc",selection.desc);
                obj.put("uri",selection.getOriginalUri().toString());
                obj.put("width",selection.width);
                obj.put("height",selection.height);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }
        private static @Nullable
        SelectedMedia decodeSelectionObject(JSONObject obj){
            SelectedMedia selection = new SelectedMedia();
            try {
                if(!obj.has("type") || !obj.has("uri")) return null;
                else{
                    selection.type = Type.fromString( obj.getString("type"));
                    selection.imageId = obj.has("imageId")?obj.getLong("imageId"):0;
                    selection.datetaken = obj.has("datetaken")?obj.getLong("datetaken"):0;
                    selection.latitude = obj.has("latitude")?obj.getDouble("latitude"):0;
                    selection.longitude =obj.has("longitude")?obj.getDouble("longitude"):0;
                    selection.desc = obj.has("desc")?obj.getString("desc"):null;
                    selection.uri = Uri.parse(obj.getString("uri"));
                    selection.width = obj.has("width")?obj.getInt("width"):0;
                    selection.height = obj.has("height")?obj.getInt("height"):0;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return selection;
        }
    }
}
