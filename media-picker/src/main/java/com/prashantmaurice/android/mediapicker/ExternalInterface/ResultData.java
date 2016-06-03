package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Intent;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the object that is generated from data object coming
 * from {@link android.app.Activity#onActivityResult(int, int, Intent)}
 */
public class ResultData {
    List<SelectionObject> selectedObjs = new ArrayList<>();

    /**
     * Returns a list of Selected Objects. There are helper functions
     * inside these objects that can be used to generate all the details
     * you need
     *
     * @return list of {@link MImageObj}
     */
    public List<SelectionObject> getSelectedPics() {
        return selectedObjs;
    }
}
