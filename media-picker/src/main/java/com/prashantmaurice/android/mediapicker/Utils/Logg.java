package com.prashantmaurice.android.mediapicker.Utils;

import android.util.Log;

/**
 * Created by maurice on 27/05/15.
 */
public class Logg {
    private static boolean showDebugI = Settings.isDebugMode;
    private static boolean showDebugD = Settings.isDebugMode;
    private static boolean showDebugV = Settings.isDebugMode;
    private static boolean showDebugE = Settings.isDebugMode;
    private static boolean showDebugT = Settings.isDebugMode;
    public static void i(String tag, String debug){
        if(showDebugI) Log.i(tag, ""+debug);
    }
    public static void d(String tag, String debug){
        if(showDebugD) Log.d(tag, ""+debug);
    }
    public static void v(String tag, String debug){
        if(showDebugV) Log.v(tag, ""+debug);
    }
    public static void e(String tag, String debug){
        if(showDebugE) Log.e(tag, ""+debug);
    }
    public static void t(String tag, String debug){
        if(showDebugT) Log.d(tag, ""+debug);
    }
    public static void m(String tag, String debug){
        if(showDebugT) Log.e(tag, ""+debug);
    }
    public static void r(String tag, String debug){
        if(showDebugT) Log.d(tag, ""+debug);
    } //for render time logs
}
