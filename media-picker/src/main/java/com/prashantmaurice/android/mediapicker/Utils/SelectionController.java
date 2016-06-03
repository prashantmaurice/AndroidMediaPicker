package com.prashantmaurice.android.mediapicker.Utils;

import com.prashantmaurice.android.mediapicker.Models.MImageObj;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by maurice on 02/06/16.
 */
public class SelectionController {
    static SelectionController instance;

    List<MImageObj> selected = new ArrayList<>();

    public static SelectionController getInstance(){
        if(instance==null) instance = new SelectionController();
        return instance;
    }

    SelectionController(){}

    public List<MImageObj> getSelectedPics() {
        return selected;
    }

    public void toggle(MImageObj MImageObj) {
        if(selected.contains(MImageObj)) selected.remove(MImageObj);
        else selected.add(MImageObj);
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
}
