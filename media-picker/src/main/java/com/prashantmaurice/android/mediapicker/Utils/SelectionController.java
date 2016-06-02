package com.prashantmaurice.android.mediapicker.Utils;

/**
 * Created by maurice on 02/06/16.
 */
public class SelectionController {
    static SelectionController instance;

    public static SelectionController getInstance(){
        if(instance==null) instance = new SelectionController();
        return instance;
    }

    SelectionController(){

    }
}
