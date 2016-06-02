package com.prashantmaurice.android.mediapicker;

import android.content.Context;
import android.content.Intent;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class MediaPicker {

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
        public static IntentData parseResult(Intent data) {
            IntentData intentData = new IntentData();

            if(data.hasExtra(IntentBuilder.INTENT_SELECTMULTIPLE)){
                intentData.selectMultiple = data.getBooleanExtra(IntentBuilder.INTENT_SELECTMULTIPLE, false);
            }


            return intentData;
        }

        //Main data object that is created with this builder
        public static class IntentData{
            private boolean selectMultiple = false;//show invite Dialog on open

            public List<String> getSelectedPics() {
                return new ArrayList<>();
            }
        }
    }
}
