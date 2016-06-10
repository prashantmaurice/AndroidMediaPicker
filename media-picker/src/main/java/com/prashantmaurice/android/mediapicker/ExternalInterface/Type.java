package com.prashantmaurice.android.mediapicker.ExternalInterface;

/**
 * Created by maurice on 03/06/16.
 */

public enum Type{
    GALLERY_IMAGE,
    CAMERA_IMAGE,
    UNKNOWN;
    public String getString(){
        switch (this){
            case GALLERY_IMAGE: return "gallery_image";
            case CAMERA_IMAGE: return "camera_image";
            default:return "unknown";
        }
    }
    public static Type fromString(String featureStr){
        for(Type value  : Type.values()) if(value.getString().equals(featureStr)) return value;
        return UNKNOWN;//default
    }
}
