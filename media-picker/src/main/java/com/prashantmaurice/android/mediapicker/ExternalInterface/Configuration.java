package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Utils.Settings;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import static com.prashantmaurice.android.mediapicker.MediaPicker.From;
import static com.prashantmaurice.android.mediapicker.MediaPicker.Pick;

/**
 * Main config object that is used to call our library.
 * This will be used by all the activities in the library
 * to modify UI/UX accordingly
 */

public class Configuration implements Parcelable {
    private static String INTENT_CONFIGURABLE = "maindata";

    private int maximumImageCount = Settings.DEFAULT_MAXCOUNT_IMAGES;//default
    private int maximumVideoCount = Settings.DEFAULT_MAXCOUNT_VIDEO;//default

    private int startFrom = 0;//default
    private Pick pick = Pick.IMAGE;
    private From from = From.GALLERY_AND_CAMERA;
    private long maxFileSize = 0; // max file size in bytes
    private List<CustomFolder> customFolders = new ArrayList<>();

    public Configuration(){}

    /** Getters and Setters */
    public void setPick(Pick pick) {
        this.pick = pick;
    }
    public void setFrom(From from) {
        this.from = from;
    }
    public void setMaximumImageCount(int maximumCount) { this.maximumImageCount = maximumCount;}
    public void setMaximumVideoCount(int maximumCount) { this.maximumVideoCount = maximumCount;}
    public void setMaximumFileSize(int maxSize) { this.maxFileSize = maxSize*1024;} // convert to bytes
    public void setStartFrom(int startFrom) { this.startFrom = startFrom;}
    public void addCustomFolders(List<CustomFolder> foldersToAdd) { customFolders.addAll(foldersToAdd); }
    public int getStartFrom() {return this.startFrom;}
    public int getMaximumImageCount() {return this.maximumImageCount;}
    public int getMaximumVideoCount() {return this.maximumVideoCount;}
    public long getMaximumFileSize() {return this.maxFileSize;}
    public List<CustomFolder> getCustomFolders() {return this.customFolders;}

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
        dest.writeInt(maximumImageCount);
        dest.writeInt(maximumVideoCount);
        dest.writeInt(startFrom);
        dest.writeSerializable(from);
        dest.writeString(pick.getStr());
        dest.writeLong(maxFileSize);
        dest.writeString(CustomFolder.encode(customFolders).toString());
    }

    public Configuration(Parcel parcel) {
        maximumImageCount = parcel.readInt();
        maximumVideoCount = parcel.readInt();
        startFrom = parcel.readInt();
        from = (From) parcel.readSerializable();
        pick = MediaPicker.Pick.getPickForPickStr(parcel.readString());
        maxFileSize = parcel.readLong();
        try {
            customFolders = CustomFolder.decode(new JSONArray(parcel.readString()));
        } catch (JSONException e) {e.printStackTrace();}
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


    public boolean isSelectMultiple() {
        return !(maximumImageCount<=1 && maximumVideoCount<=1);
    }
}
