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

    public List<ImageObj> getSelectedPics() {
        return selected;
    }

    public void toggle(ImageObj imageObj) {
        if(selected.contains(imageObj)) selected.remove(imageObj);
        else selected.add(imageObj);
    }

    public boolean isSelected(ImageObj imageObj) {
        return selected.contains(imageObj);
    }

    public int getSelectNumber(ImageObj imageObj) {
        return selected.indexOf(imageObj);
    }
}
