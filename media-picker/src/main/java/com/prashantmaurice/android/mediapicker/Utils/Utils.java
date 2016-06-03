package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by maurice on 03/06/16.
 */
public class Utils {

    public static class FileStorage{
        public static @Nullable Uri getOutputMediaFileUri() {
            if(getOutputMediaFile()==null) return null;
            return Uri.fromFile(getOutputMediaFile());
        }

        private static File getOutputMediaFile() {

            // External sdcard location
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                    Settings.SDCARD_IMG_FOLDER);

            // Create the storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Logg.e("ERROR", "Oops! Failed create Image directory to store images");
                    return null;
                }
            }

            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());
            File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

            return mediaFile;
        }
    }

    public static class Toasts{
        public static void show(Context context, String detailedText) {
            Toast.makeText(context,detailedText, Toast.LENGTH_SHORT).show();
        }
    }


}
