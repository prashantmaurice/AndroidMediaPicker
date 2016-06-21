package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.MediaObj;
import com.prashantmaurice.android.mediapicker.Utils.Utils.SerializeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SelectionController implements Parcelable{
    String TAG = "BAD";
    static SelectionController instance;

    List<MediaObj> selected = new ArrayList<>();

    public static SelectionController getInstance(){
        if(instance==null) instance = new SelectionController();
        return instance;
    }

    SelectionController(){
        Logg.d(TAG,"Initialized");
    }

    public List<MediaObj> getSelectedMedias() {
        return selected;
    }

    public void toggle(Context context, MediaObj mediaObj) {
        if(selected.contains(mediaObj)){
            selected.remove(mediaObj);
        }else{
            if(selected.size()>= FolderActivity.getConfiguration().getMaximumCount()){
                Configuration config = FolderActivity.getConfiguration();
                String mediaTypeText = "images";
                if(config.getPick().equals(MediaPicker.Pick.VIDEO)) mediaTypeText = "videos";
                int maxcount = FolderActivity.getConfiguration().getMaximumCount();
                int startCount = FolderActivity.getConfiguration().getStartFrom();
                String text = "You can only select "+ (maxcount+startCount)+" "+mediaTypeText;
                ToastMain.showSmarterToast(context,null,text);
            }else{
                selected.add(mediaObj);
            }
        }
    }

    public boolean isSelected(MediaObj mediaObj) {
        return selected.contains(mediaObj);
    }

    public int getSelectNumber(MediaObj mediaObj) {
        return FolderActivity.getConfiguration().getStartFrom()+1+selected.indexOf(mediaObj);
    }

    public void clearSelection() {
        selected.clear();
    }

    public void reset() {
        clearSelection();
    }




    /** Serialization Functions */
    public static final SelectionController.Creator CREATOR = new SelectionController.Creator() {
        public SelectionController createFromParcel(Parcel in ) { return new SelectionController(in);}
        public SelectionController[] newArray(int size) {return new SelectionController[size];}
    };
    @Override
    public int describeContents() {return 0;}

    public SelectionController(Parcel in ) {
        selected.addAll(SerializeUtils.<MediaObj>readArray(in,MediaObj.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SerializeUtils.writeArray(dest,selected,flags);
    }


}
