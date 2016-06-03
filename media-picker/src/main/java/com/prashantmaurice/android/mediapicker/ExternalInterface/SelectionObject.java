package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.net.Uri;

/**
 * Created by maurice on 03/06/16.
 */
public class SelectionObject {
    Uri uri;
    Type type;

    /**
     * Get the type of object.
     *
     * {@link Type#IMAGE} == Image
     * {@link Type#UNKNOWN} == Unknown
     *
     */
    public Type getType(){
        return type;
    }

    /**
     * @return Is this object an image
     */
    public boolean isImage(){
        return type.equals(Type.IMAGE);
    }

    /**
     * Get Uri for the Media selected
     */
    public  Uri getUri(){
        return uri;
    }
}
