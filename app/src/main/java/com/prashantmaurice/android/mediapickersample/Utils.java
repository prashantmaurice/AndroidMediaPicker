package com.prashantmaurice.android.mediapickersample;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by maurice on 02/06/16.
 */
public class Utils {

    public static void showToast(Context context, String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }

}
