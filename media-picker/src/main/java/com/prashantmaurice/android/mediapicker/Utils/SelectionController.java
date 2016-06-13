package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity.FolderActivity;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Utils.Utils.SerializeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SelectionController implements Parcelable{
    String TAG = "BAD";
    static SelectionController instance;

    List<MImageObj> selected = new ArrayList<>();

    public static SelectionController getInstance(){
        if(instance==null) instance = new SelectionController();
        return instance;
    }

    SelectionController(){
        Logg.d(TAG,"Initialized");
    }

    public List<MImageObj> getSelectedPics() {
        return selected;
    }

    public void toggle(Context context, MImageObj MImageObj) {
        if(selected.contains(MImageObj)){
            selected.remove(MImageObj);
        }else{
            if(selected.size()>= FolderActivity.getConfiguration().getMaximumCount()){
                ToastMain.showSmarterToast(context,null,"You can only select "+ FolderActivity.getConfiguration().getMaximumCount());
            }else{
                selected.add(MImageObj);
            }
        }
    }

    public boolean isSelected(MImageObj MImageObj) {
        return selected.contains(MImageObj);
    }

    public int getSelectNumber(MImageObj MImageObj) {
        return 1+selected.indexOf(MImageObj);
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
        selected.addAll(SerializeUtils.<MImageObj>readArray(in,MImageObj.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        SerializeUtils.writeArray(dest,selected,flags);
    }


}
