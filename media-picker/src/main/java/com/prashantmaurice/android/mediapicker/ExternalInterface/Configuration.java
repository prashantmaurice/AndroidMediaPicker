package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Context;
import android.content.Intent;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;

/**
 * Main config object that is used to call our library.
 * This will be used by all the activities in the library
 * to modify UI/UX accordingly
 */

public class Configuration {
    private static String INTENT_SELECTMULTIPLE = "selectmultiple";
    private boolean selectMultiple = false;//show invite Dialog on open

    public Intent build(Context context){
        Intent intent = new Intent(context, FolderActivity.class);
        intent.putExtra(INTENT_SELECTMULTIPLE, selectMultiple);
        return intent;
    }

    public void setSelectMultiple(boolean selectMultiple) {
        this.selectMultiple = selectMultiple;
    }

    protected static Configuration parseResult(Intent data) {
        Configuration configuration = new Configuration();

        if(data.hasExtra(INTENT_SELECTMULTIPLE)){
            configuration.selectMultiple = data.getBooleanExtra(INTENT_SELECTMULTIPLE, false);
        }


        return configuration;
    }
}
