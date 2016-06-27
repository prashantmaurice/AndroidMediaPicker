package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.MediaStore;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Type;
import com.prashantmaurice.android.mediapicker.MediaPicker;

/**
 * Created by dipakb on 20/06/16.
 */
public class MVideoObj extends MediaObj implements Parcelable{

    private Uri mainUri;//you can stream main video from here
    private Type type;//use this to distinguish from media sources
    private long videoId;
    private long dateTaken;
    private double latitude;
    private double longitude;
    private String desc;
    private int width;
    private int height;
    private int duration;
    private MediaPicker.Pick mediaType = MediaPicker.Pick.VIDEO;
    private long size;


    public MVideoObj(){}

    /** Abstract Getters */
    public Type getType() {return type;}
    public long getMediaId() {return videoId;}
    public long getDateTaken() {return dateTaken;}
    public String getDesc() {return desc;}
    public Uri getMainUri() {return mainUri;}
    public String getPath() {return getMainUri().getPath();}
    public MediaPicker.Pick getMediaType(){return mediaType;}
    public long getSize() {return size;}

    /** Abstract Setters */
    public void setType(Type type) {this.type = type;}
    public void setMediaId(long videoId) {this.videoId = videoId;}
    public void setDateTaken(long dateTaken) {this.dateTaken = dateTaken;}
    public void setDesc(String desc) {this.desc = desc;}
    public void setMainUri(Uri mainUri) {this.mainUri = mainUri;}
    public void setSize(long size) {this.size = size;}

    /**Video Obj Getters*/
    public double getLatitude() {return latitude;}
    public double getLongitude() {return longitude;}
    public int getWidth() {return width;}
    public int getHeight() {return height;}
    public int getDuration() {return duration;}

    /**Video Obj Setters*/
    public void setLatitude(double latitude) {this.latitude = latitude;}
    public void setLongitude(double longitude) {this.longitude = longitude;}
    public void setWidth(int width) {this.width = width;}
    public void setHeight(int height) {this.height = height;}
    public void setDuration(int duration) {this.duration = duration;}

    @Override
    public boolean equals(Object o){
        if (o instanceof MVideoObj){
            MVideoObj c = (MVideoObj) o;
            if (this.mainUri.getPath().equals(c.mainUri.getPath())) return true;
        }
        return false;
    }

    public static class Builder{
        public static MVideoObj generateFromMediaVideoCursor(long id, long dateTaken, int width, int height, double lat, double longg, String desc, int duration, long size) {
            Uri mainUri = Uri.withAppendedPath(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, Long.toString(id));
            MVideoObj mVideoObj = new MVideoObj();
            mVideoObj.setType(Type.GALLERY_VIDEO);
            mVideoObj.setMediaId(id);
            mVideoObj.setDuration(duration);
            mVideoObj.setDateTaken(dateTaken);
            mVideoObj.setLatitude(lat);
            mVideoObj.setLongitude(longg);
            mVideoObj.setDesc(desc);
            mVideoObj.setMainUri(mainUri);
            mVideoObj.setWidth(width);
            mVideoObj.setHeight(height);
            mVideoObj.setSize(size);
            return mVideoObj;

        }
    }


    /** Serialization Functions */
    public static final MVideoObj.Creator CREATOR = new MVideoObj.Creator() {
        public MVideoObj createFromParcel(Parcel in ) { return new MVideoObj(in);}
        public MVideoObj[] newArray(int size) {return new MVideoObj[size];}
    };
    @Override
    public int describeContents() {return 0;}

    public MVideoObj(Parcel in ) {
        type = (Type) in.readSerializable();
        videoId =  in.readLong();
        dateTaken =  in.readLong();
        latitude =  in.readDouble();
        longitude =  in.readDouble();
        duration = in.readInt();
        desc =  in.readString();
        mainUri = Uri.parse( in.readString());
        height = in.readInt();
        width = in.readInt();
        size = in.readLong();
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(type);
        dest.writeLong(videoId);
        dest.writeLong(dateTaken);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeInt(duration);
        dest.writeString(desc);
        dest.writeString(mainUri.toString());
        dest.writeInt(height);
        dest.writeInt(width);
        dest.writeLong(size);
    }
}
