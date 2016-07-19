package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Intent;
import android.net.Uri;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;

import java.util.ArrayList;
import java.util.List;

/**`
 * This is the object that is generated from data object coming
 * from {@link android.app.Activity#onActivityResult(int, int, Intent)}
 */
public class ResultData {
    List<SelectedMedia> selectedObjs = new ArrayList<>();
    String customFolderSelected;

    /**
     * Returns a list of Selected Objects. There are helper functions
     * inside these objects that can be used to generate all the details
     * you need
     *
     * @return list of {@link MImageObj}
     */
    public List<SelectedMedia> getSelectedMedias() {
        return selectedObjs;
    }

    /**
     * Return originalUri of media that is selected. You can access bitmaps from these using
     *
     */
    public List<Uri> getSelectedOriginalUri() {
        List<Uri> uris = new ArrayList<>();
        for(SelectedMedia obj : selectedObjs) uris.add(obj.getOriginalUri());
        return uris;
    }


    public boolean isCustomSelected() {
        return customFolderSelected!=null;
    }

    public String getCustomSelected() {
        return customFolderSelected;
    }
}
