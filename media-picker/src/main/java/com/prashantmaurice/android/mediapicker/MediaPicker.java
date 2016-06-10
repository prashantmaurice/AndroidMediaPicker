package com.prashantmaurice.android.mediapicker;

import android.content.Context;
import android.content.Intent;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultData;
import com.prashantmaurice.android.mediapicker.ExternalInterface.ResultDataBuilder;

/**
 * This is the main Interface to use this library. Don't call any other activity directly
 */
public class MediaPicker {
    public enum Pick{IMAGE}
    public enum From{GALLERY_AND_CAMERA}

    /**
     * This is used to parse the Result generated from this Picker.
     * It gives you a neatly formatted object through which you can
     * retrieve all the information you need
     *
     */
    public static ResultData getResultData(Intent data){
        return ResultDataBuilder.parseResult(data);
    }

    /**
     * THis is the main Builder to build an intent object to call this library
     */
    public static class IntentBuilder{
        Configuration config = new Configuration();
        public IntentBuilder(){};

        /**
         * Select multiple media or single media
         */
        public IntentBuilder selectMultiple(boolean selectMultiple) {
            config.setSelectMultiple(selectMultiple);
            return this;
        }

        /**
         * Select multiple media or single media
         */
        public IntentBuilder setMaximumCount(int maxCount) {
            config.setMaximumCount(maxCount);
            return this;
        }

        /**
         * Pick which kind of media
         */
        public IntentBuilder pick(Pick pick) {
            config.setPick(pick);
            return this;
        }

        /**
         * Pick media from where
         */
        public IntentBuilder from(From maxCount) {
            config.setFrom(maxCount);
            return this;
        }


        /**
         * Generate Intent with selected parameters
         */
        public Intent build(Context context){
            return config.build(context);
        }
    }
}
