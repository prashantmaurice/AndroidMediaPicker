
package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Type;
import com.prashantmaurice.android.mediapicker.MediaPicker;

/**
 * Created by maurice on 02/06/16.
 *
 * This is used for custom folders
 */
public class MCustomObj extends MediaObj implements Parcelable {

    private Uri mainUri;//you can stream main image from here
    private Type type;//use this to distinguish from media sources
    private long imageId;
    private long dateTaken;
    private String desc;
    private int width;
    private int height;
    private int orientation;
    private MediaPicker.Pick mediaType = MediaPicker.Pick.IMAGE;
    private long size;

    public MCustomObj(){}

    /** Abstract Getters */
    public Type getType() {return type;}
    public long getMediaId() {return imageId;}
    public long getDateTaken() {return dateTaken;}
    public String getDesc() {return desc;}
    public Uri getMainUri() {return mainUri;}
    public String getPath() {return getMainUri().getPath();}
    public MediaPicker.Pick getMediaType() { return mediaType;}
    public long getSize() {return size;}

    /** Abstract Setters */
    public void setType(Type type) {this.type = type;}
    public void setMediaId(long imageId) {this.imageId = imageId;}
    public void setDateTaken(long dateTaken) {this.dateTaken = dateTaken;}
    public void setDesc(String desc) {this.desc = desc;}
    public void setMainUri(Uri mainUri) {this.mainUri = mainUri;}
    public void setOrientation(int orientation) {this.orientation = orientation;}
    public void setSize(long size) {this.size = size;}

    /**Image Obj Getters*/

    /**Image Obj Setters*/

    @Override
    public boolean equals(Object o){
        if (o instanceof MCustomObj){
            MCustomObj c = (MCustomObj) o;
            if (this.mainUri.getPath().equals(c.mainUri.getPath())) return true;
        }
        return false;
    }

    public static class Builder{
        public static MCustomObj generateFromMediaImageCursor(long id, long dateTaken, int width, int height, double lat, double longg, String desc, int orientation, long size) {
            Uri mainUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(id));

            MCustomObj mImageObj = new MCustomObj();
            mImageObj.setType(Type.GALLERY_IMAGE);
            mImageObj.setMediaId(id);
            mImageObj.setOrientation(orientation);
            mImageObj.setDateTaken(dateTaken);
            mImageObj.setDesc(desc);
            mImageObj.setMainUri(mainUri);
            mImageObj.setSize(size);
            return mImageObj;

        }
    }


    /** Serialization Functions */
    public static final Creator CREATOR = new Creator() {
        public MCustomObj createFromParcel(Parcel in ) { return new MCustomObj(in);}
        public MCustomObj[] newArray(int size) {return new MCustomObj[size];}
    };
    @Override
    public int describeContents() {return 0;}

    public MCustomObj(Parcel in ) {
        type = (Type) in.readSerializable();
        imageId =  in.readLong();
        dateTaken =  in.readLong();
        orientation = in.readInt();
        desc =  in.readString();
        mainUri = Uri.parse( in.readString());
        height = in.readInt();
        width = in.readInt();
        size = in.readLong();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeLong(imageId);
        dest.writeLong(dateTaken);
        dest.writeInt(orientation);
        dest.writeString(desc);
        dest.writeString(mainUri.toString());
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeLong(size);
    }
}
