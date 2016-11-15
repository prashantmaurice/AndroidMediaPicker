package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;

/**
 *  Use this to show toasts to user, this shows small text if in production mode and shows detailed toast
 *  in debug mode for testers to identify problems.
 *
 */

public class ToastMain2 {

    public static void showSmarterToast(Context context, String debugText, String text ){

        // Production Toast
        if( text != null && text.length() > 0 )
            Utils.Toasts.show( context, text );

        // Non - Production Toast
        if( Settings.showDebugToasts && debugText != null && debugText.length() > 0 )
            Utils.Toasts.show( context, "D: " + debugText );
    }

}
