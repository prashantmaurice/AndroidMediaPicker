package com.prashantmaurice.android.mediapicker.Models;

import android.net.Uri;
import android.provider.MediaStore;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Type;

/**
 * Created by maurice on 02/06/16.
 */
public class MImageObj {

    private Uri mainUri;//you can stream main image from here
    private long mediaIdForThumbNail;//you can use this to get thumbnail Bitmap
    private Type type;//use this to distinguish from media sources

    public MImageObj(){}

    public Uri getMainUri() {
        return mainUri;
    }
    public long getThumbnailId() {
        return mediaIdForThumbNail;
    }
    public void setMainUri(Uri mainUri) {
        this.mainUri = mainUri;
    }
    public void setThumbnailId(long id) {
        this.mediaIdForThumbNail = id;
    }
    public Type getType() {
        return type;
    }
    public void setType(Type type) {
        this.type = type;
    }


//    public Uri getURI(){
//        return Uri.withAppendedPath( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(id));
////        return Uri.parse("content://com.android.providers.media.documents/document/image%3A35540");
////        if(id!=0) return Uri.fromFile(new File("/document/image:"+id));
////        return Uri.fromFile(new File(getPath()));
//    }


    @Override
    public boolean equals(Object o){
        if (o instanceof MImageObj){
            MImageObj c = (MImageObj) o;
            if (this.mainUri.getPath().equals(c.mainUri.getPath())) return true;
        }
        return false;
    }


    public String getPath() {
        return getMainUri().getPath();
    }


    public static class Builder{
        public static MImageObj generateFromMediaImageCursor(long id){
            Uri mainUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, Long.toString(id));
            MImageObj mImageObj = new MImageObj();
            mImageObj.setType(Type.GALLERY_IMAGE);
            mImageObj.setThumbnailId(id);
            mImageObj.setMainUri(mainUri);
            return mImageObj;

        }
        public static MImageObj generateFromCameraResult(Uri cameraURI) {
            MImageObj mImageObj = new MImageObj();
            mImageObj.setType(Type.CAMERA_IMAGE);
            mImageObj.setMainUri(cameraURI);
            return mImageObj;
        }
    }
}
