package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Type;

/**
 * Created by maurice on 02/06/16.
 */
public class MImageObj implements Parcelable {

    private Uri mainUri;//you can stream main image from here
    private Type type;//use this to distinguish from media sources
    private long imageId;
    private long dateTaken;
    private double latitude;
    private double longitude;
    private String desc;
    private int width;
    private int height;
    private int orientation;

    public MImageObj(){}

    /** Getters */
    public Type getType() {return type;}
    public long getImageId() {return imageId;}
    public long getDateTaken() {return dateTaken;}
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public String getDesc() {return desc;}
    public Uri getMainUri() {return mainUri;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public String getPath() {return getMainUri().getPath();}

    /** Setters */
    public void setType(Type type) {this.type = type;}
    public void setImageId(long imageId) {this.imageId = imageId;}
    public void setDateTaken(long dateTaken) {this.dateTaken = dateTaken;}
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setDesc(String desc) {this.desc = desc;}
    public void setMainUri(Uri mainUri) {this.mainUri = mainUri;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}


    @Override
    public boolean equals(Object o){
        if (o instanceof MImageObj){
            MImageObj c = (MImageObj) o;
            if (this.mainUri.getPath().equals(c.mainUri.getPath())) return true;
        }
        return false;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public int getOrientation() {
        return orientation;
    }


    public static class Builder{
        public static MImageObj generateFromMediaImageCursor(long id, long dateTaken, int width, int height, double lat, double longg, String desc, int orientation) {
            Uri mainUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(id));
            MImageObj mImageObj = new MImageObj();
            mImageObj.setType(Type.GALLERY_IMAGE);
            mImageObj.setImageId(id);
            mImageObj.setOrientation(orientation);
            mImageObj.setDateTaken(dateTaken);
            mImageObj.setLatitude(lat);
            mImageObj.setLongitude(longg);
            mImageObj.setDesc(desc);
            mImageObj.setMainUri(mainUri);
            mImageObj.setWidth(width);
            mImageObj.setHeight(height);
            return mImageObj;

        }
        public static MImageObj generateFromCameraResult(Uri cameraURI) {
            MImageObj mImageObj = new MImageObj();
            mImageObj.setType(Type.CAMERA_IMAGE);
            mImageObj.setDateTaken(System.currentTimeMillis());
            mImageObj.setMainUri(cameraURI);
            return mImageObj;
        }
    }


    /** Serialization Functions */
    public static final MImageObj.Creator CREATOR = new MImageObj.Creator() {
        public MImageObj createFromParcel(Parcel in ) { return new MImageObj(in);}
        public MImageObj[] newArray(int size) {return new MImageObj[size];}
    };
    @Override
    public int describeContents() {return 0;}

    public MImageObj(Parcel in ) {
        type = (Type) in.readSerializable();
        imageId =  in.readLong();
        dateTaken =  in.readLong();
        latitude =  in.readDouble();
        longitude =  in.readDouble();
        orientation = in.readInt();
        desc =  in.readString();
        mainUri = Uri.parse( in.readString());
        height = in.readInt();
        width = in.readInt();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeLong(imageId);
        dest.writeLong(dateTaken);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(orientation);
        dest.writeString(desc);
        dest.writeString(mainUri.toString());
        dest.writeInt(height);
        dest.writeInt(width);
    }
}
