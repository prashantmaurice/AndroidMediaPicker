package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Created by maurice on 03/06/16.
 */
public class Utils {

    public static class FileStorage{
        public static @Nullable Uri getOutputMediaFileUri() {
            File output = getOutputMediaFile();
            if(output==null) return null;
            return Uri.fromFile(output);
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

    //
    public static Bitmap createScaledBitmap(Bitmap bm, int maxSize) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        if (width > height) {
            // landscape
            float ratio = (float) width / maxSize;
            width = maxSize;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxSize;
            height = maxSize;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxSize;
            width = maxSize;
        }

        bm = Bitmap.createScaledBitmap(bm, width, height, true);
        return bm;
    }


    public static class SerializeUtils {
        static void writeArray(Parcel out, List<?> selected, int flags){
            out.writeInt(selected.size());
            for(Object s : selected) out.writeParcelable((Parcelable) s,flags);
        }


        public static <E> List<E> readArray(Parcel in, ClassLoader classLoader){
            List<E> arr = new ArrayList<>();
            int size =  in.readInt();
            for(int i=0;i<size;i++) arr.add((E) in.readParcelable(classLoader));
            return arr;
        }
    }

    public static <T> List<T> toList(T obj) {
        List<T> list = new ArrayList<>();
        list.add(obj);
        return list;
    }

    public static JSONObject stripJsonOfStringNull(JSONObject jsonObject ){
        JSONObject newJsonObject = new JSONObject();
        Iterator<String> iter = jsonObject.keys();
        while (iter.hasNext()) {
            String key = iter.next();
            try {
                Object value = jsonObject.get(key);
                if( value instanceof JSONObject )
                    newJsonObject.put( key, stripJsonOfStringNull( (JSONObject)value ) );
                else {
                    String strValOfValue = value.toString();
                    if( !strValOfValue.equals("null") )
                        newJsonObject.put( key, value );
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return newJsonObject;
    }
}
