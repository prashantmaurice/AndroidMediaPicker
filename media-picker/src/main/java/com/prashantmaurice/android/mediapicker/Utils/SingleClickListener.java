package com.prashantmaurice.android.mediapicker.Utils;

import android.view.View;

/**
 * Documented by Maurice :
 *
 *  This class is used to block double click on views. It does that by Banning two clicks within 1 sec
 *
 */
public abstract class SingleClickListener implements View.OnClickListener {

    long lastTimeCLicked = 0;
    int MIN_CLICK_SPAN = 1000; //difference between two click times

    @Override
    public void onClick(View view) {
        if(System.currentTimeMillis()-lastTimeCLicked>MIN_CLICK_SPAN){
            onSingleClick(view);
            lastTimeCLicked = System.currentTimeMillis();
        }
    }

    public abstract void onSingleClick(View view);
}
