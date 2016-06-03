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
    public static List<SelectionObject> generateSelectionObject(List<MImageObj> objArr){
        List<SelectionObject> list = new ArrayList<>();
        for(MImageObj obj : objArr) list.add(generateSelectionObject(obj));
        return list;
    }

    public static SelectionObject generateSelectionObject(MImageObj obj){
        SelectionObject selection = new SelectionObject();
        selection.uri = obj.getURI();
        selection.type = Type.IMAGE;
        return selection;
    }

    static class Encoders{
        public static ResultData decode(JSONObject obj){
            ResultData activityObject = new ResultData();
            try {
                JSONArray arr = obj.getJSONArray("objects");
                ArrayList<SelectionObject> objects = new ArrayList<>();
                for(int i=0;i<arr.length();i++){
                    SelectionObject ob2j = decodeSelectionObject(arr.getJSONObject(i));
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
                for(SelectionObject selection : resultData.getSelectedPics()) objects.put(encodeSelectionObject(selection));
                obj.put("objects",objects);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }
        private static JSONObject encodeSelectionObject(SelectionObject selection){
            JSONObject obj = new JSONObject();
            try {
                obj.put("type",selection.getType().getString());
                obj.put("uri",selection.getUri().toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return obj;
        }
        private static @Nullable  SelectionObject decodeSelectionObject(JSONObject obj){
            SelectionObject activityObject = new SelectionObject();
            try {
                if(!obj.has("type") || !obj.has("uri")) return null;
                else{
                    activityObject.type = Type.fromString( obj.getString("type"));
                    activityObject.uri = Uri.parse(obj.getString("uri"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return activityObject;
        }
    }
}
