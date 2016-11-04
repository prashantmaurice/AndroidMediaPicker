package com.prashantmaurice.android.mediapicker.ExternalInterface;

import com.prashantmaurice.android.mediapicker.Models.FolderObj;

/**
 * Created by maurice on 04/11/16.
 */

public class CaptureImgFolder extends FolderObj {

    @Override
    public String getName() {
        return "Capture from camera";
    }

    @Override
    public String getBGUri() {
        return null;
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
