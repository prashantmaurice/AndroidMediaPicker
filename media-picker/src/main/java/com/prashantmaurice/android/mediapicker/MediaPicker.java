package com.prashantmaurice.android.mediapicker;

import android.content.Context;
import android.content.Intent;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.Models.ImageObj;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class MediaPicker {

    public static class ResultParser{
        private static String INTENT_SELECTED = "selected";

        //Main data object that is created with this builder
        public static class ResultData{
            List<ImageObj> images = new ArrayList<>();
            public List<ImageObj> getSelectedPics() {
                return images;
            }
            public void setSelectedPics(List<ImageObj> images){
                this.images.clear();
                this.images.addAll(images);
            }

            public Intent build() {
                Intent intent = new Intent();
                intent.putExtra(INTENT_SELECTED, ImageObj.encode(images).toString());
                return intent;
            }
        }

        //Use this to parse returned data
        public static ResultData parseResult(Intent data) {
            ResultData intentData = new ResultData();

            if(data.hasExtra(INTENT_SELECTED)){
                String jsonArrStr =  data.getStringExtra(INTENT_SELECTED);
                try {
                    JSONArray jsonArr = new JSONArray(jsonArrStr);
                    intentData.images.addAll(ImageObj.decodeFromServer(jsonArr));
                } catch (JSONException e) {e.printStackTrace();}
            }

            return intentData;
        }
    }


    //Use this to build intent for calling this class
    public static class IntentBuilder{
        private static String INTENT_SELECTMULTIPLE = "selectmultiple";

        IntentData intentData = new IntentData();

        public IntentBuilder(){};

        public IntentBuilder selectMultiple(boolean selectMultiple) {
            intentData.selectMultiple = selectMultiple;
            return this;
        }

        public Intent build(Context context){
            Intent intent = new Intent(context, FolderActivity.class);
            intent.putExtra(INTENT_SELECTMULTIPLE, intentData.selectMultiple);
            return intent;
        }

        //Use this to parse returned data
        protected static IntentData parseResult(Intent data) {
            IntentData intentData = new IntentData();

            if(data.hasExtra(IntentBuilder.INTENT_SELECTMULTIPLE)){
                intentData.selectMultiple = data.getBooleanExtra(IntentBuilder.INTENT_SELECTMULTIPLE, false);
            }


            return intentData;
        }

        //Main data object that is created with this builder
        public static class IntentData{
            private boolean selectMultiple = false;//show invite Dialog on open
        }
    }
}
