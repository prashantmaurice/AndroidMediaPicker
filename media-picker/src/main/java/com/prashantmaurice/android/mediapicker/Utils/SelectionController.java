package com.prashantmaurice.android.mediapicker.Utils;

import com.prashantmaurice.android.mediapicker.Models.ImageObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SelectionController {
    static SelectionController instance;

    List<ImageObj> selected = new ArrayList<>();

    public static SelectionController getInstance(){
        if(instance==null) instance = new SelectionController();
        return instance;
    }

    SelectionController(){}

    public void addInSelected(ImageObj imageObj){
        selected.add(imageObj);
    }
    public void removeFromSelected(ImageObj imageObj){
        selected.remove(imageObj);
    }

    public List<ImageObj> getSelectedPics() {
        return selected;
    }
}
