package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Utils.Settings;

import static com.prashantmaurice.android.mediapicker.MediaPicker.From;
import static com.prashantmaurice.android.mediapicker.MediaPicker.Pick;

/**
 * Main config object that is used to call our library.
 * This will be used by all the activities in the library
 * to modify UI/UX accordingly
 */

public class Configuration implements Parcelable {
    private static String INTENT_CONFIGURABLE = "maindata";

    private boolean selectMultiple = false;//show invite Dialog on open
    private int maximumCount = Settings.DEFAULT_MAXCOUNT;//default
    private int startFrom = 0;//default
    private Pick pick = Pick.IMAGE;
    private From from = From.GALLERY_AND_CAMERA;
    private long maxFileSize = 0; // max file size in bytes

    public Configuration(){}

    /** Getters and Setters */
    public void setPick(Pick pick) {
        this.pick = pick;
    }
    public void setFrom(From from) {
        this.from = from;
    }
    public void setSelectMultiple(boolean selectMultiple) {
        this.selectMultiple = selectMultiple;
    }
    public void setMaximumCount(int maximumCount) { this.maximumCount = maximumCount;}
    public void setMaximumFileSize(int maxSize) { this.maxFileSize = maxSize*1024;} // convert to bytes
    public void setStartFrom(int startFrom) { this.startFrom = startFrom;}
    public int getStartFrom() {return this.startFrom;}
    public int getMaximumCount() {return this.maximumCount;}
    public boolean isSelectMultiple() { return selectMultiple;}
    public long getMaximumFileSize() {return this.maxFileSize;}

    /** Parsable logic */
    public Intent build(Context context){
        Intent intent = new Intent(context, FolderActivity.class);
        intent.putExtra(INTENT_CONFIGURABLE, this);
        return intent;
    }
    public static Configuration parseResult(Intent data) {
        if(!data.hasExtra(INTENT_CONFIGURABLE)) return new Configuration();
        else return (Configuration) data.getParcelableExtra(INTENT_CONFIGURABLE);
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(maximumCount);
        dest.writeInt(startFrom);
        dest.writeSerializable(from);
        dest.writeInt(selectMultiple?1:0);
        dest.writeString(pick.getStr());
        dest.writeLong(maxFileSize);

    }

    public Configuration(Parcel parcel) {
        maximumCount = parcel.readInt();
        startFrom = parcel.readInt();
        from = (From) parcel.readSerializable();
        selectMultiple = parcel.readInt()==1;
        pick = MediaPicker.Pick.getPickForPickStr(parcel.readString());
        maxFileSize = parcel.readLong();
    }

    public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
        @Override
        public Configuration[] newArray(int size) {
            return new Configuration[size];
        }
        @Override
        public Configuration createFromParcel(Parcel source) {
            return new Configuration(source);
        }
    };


    public From getFrom() {
        return from;
    }

    public Pick getPick() {
        return pick;
    }
}
