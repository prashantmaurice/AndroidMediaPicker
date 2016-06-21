package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Type;
import com.prashantmaurice.android.mediapicker.MediaPicker;

/**
 * Created by dipakb on 20/06/16.
 */
public abstract class MediaObj {

    public abstract long getMediaId();

    public abstract Type getType();

    public abstract long getDateTaken();

    public abstract String getDesc();

    public abstract Uri getMainUri();

    public abstract String getPath();

    public abstract MediaPicker.Pick getMediaType();

    // set
    public abstract void setMediaId(long mediaId);

    public abstract void setType(Type type);

    public abstract void setDateTaken(long dateTaken);

    public abstract void setDesc(String desc);

    public abstract void setMainUri(Uri mainUri);

}
