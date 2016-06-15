package com.prashantmaurice.android.mediapicker.ExternalInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by maurice on 03/06/16.
 */
public class SelectedMedia {
    Uri uri;
    Type type;
    long imageId;
    int width;
    int height;
    long datetaken;
    double latitude;

    /**
     * @return height of image/video if available
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return width of image/video if available
     */
    public int getWidth() {
        return width;
    }
    /**
     * @return longitude of the image taken
     */
    public double getLongitude() {
        return longitude;
    }

    /**
     * @return latitude of the image taken
     */
    public double getLatitude() {
        return latitude;
    }

    /**
     * @return date of the image taken
     */
    public long getDatetaken() {
        return datetaken;
    }

    /**
     * @return desc of the image
     */
    public String getDesc() {
        return desc;
    }

    /**
     * @return Uri of the image
     */
    public Uri getUri() {
        return uri;
    }

    /**
     * @return Uri of the image
     */
    public long getImageId() {
        return imageId;
    }


    public double longitude;
    public String desc;


    /**
     * Get the type of object.
     *
     * {@link Type#GALLERY_IMAGE} == Image
     * {@link Type#CAMERA_IMAGE} == Image
     * {@link Type#UNKNOWN} == Unknown
     *
     */
    public Type getType(){
        return type;
    }

    /**
     * @return Is this object an image
     */
    public boolean isImage(){
        return type.equals(Type.GALLERY_IMAGE)||type.equals(Type.CAMERA_IMAGE);
    }

    /**
     * Get Uri for the Media selected
     * You can get Original Bitmap Directly using {@link #getOriginalBitmap}
     */
    public Uri getOriginalUri(){
        return uri;
    }


    /**
     * This returns bitmap of original content,
     * Note : run this in async task else OutOfMemory issues might rise
     */
    public Bitmap getOriginalBitmap(Context context) throws IOException {
        InputStream inputStream = context.getContentResolver().openInputStream(uri);
        Bitmap bmp = BitmapFactory.decodeStream(inputStream);
        if( inputStream != null ) inputStream.close();
        return bmp;
    }

    /**
     * Get Bitmap of ThumbNail,
     * Note : run this in async task else OutOfMemory issues might rise
     */
    public Bitmap getThumbNail(Context context){
        if(!hasThumbNail()) return null;
        return MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), getImageId(), MediaStore.Images.Thumbnails.MINI_KIND, null);
    }

    public boolean hasThumbNail(){
        return type.equals(Type.GALLERY_IMAGE) && imageId!=0;
    }
}

