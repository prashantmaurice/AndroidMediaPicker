package com.prashantmaurice.android.mediapicker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.provider.MediaStore;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;
import com.squareup.picasso.RequestHandler;

import java.io.IOException;

/**
 * Created by maurice on 19/07/16.
 */
public class PicassoUtils {

    public static class VideoThumbnailRequestHandler extends RequestHandler {
        private final Context context;
        public static String SCHEME ="videothumb";

        public VideoThumbnailRequestHandler(Context context) {
            this.context = context;
        }

        @Override
        public boolean canHandleRequest(Request data){
            String scheme = data.uri.getScheme();
            return (SCHEME.equals(scheme));
        }

        @Override
        public Result load(Request data, int arg1) throws IOException {
            Bitmap bm = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), Long.parseLong(data.uri.getHost()), MediaStore.Video.Thumbnails.MINI_KIND, null);
//            Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            return new Result(bm, Picasso.LoadedFrom.DISK);
        }
    }

    public static class ImageThumbnailRequestHandler extends RequestHandler {
        private final Context context;
        private static String SCHEME ="imagethumb";

        //query params
        static final String ROTATE = "rotate";

        public static String getUriRequest(long mediaID, int degrees){
            return new Uri.Builder()
                    .scheme(SCHEME)
                    .authority(""+mediaID)
                    .appendQueryParameter(ROTATE,""+degrees)
                    .build().toString();
        }

        public ImageThumbnailRequestHandler(Context context) {
            this.context = context;
        }

        @Override
        public boolean canHandleRequest(Request data){
            String scheme = data.uri.getScheme();
            return (SCHEME.equals(scheme));
        }

        @Override
        public Result load(Request data, int arg1) throws IOException {
            long mediaId = Long.parseLong(data.uri.getAuthority());
            int orientation = Integer.parseInt(data.uri.getQueryParameter(ROTATE));


            Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(context.getContentResolver(), mediaId, MediaStore.Images.Thumbnails.MINI_KIND, null);
            if (orientation == 0) return new Result(bm, Picasso.LoadedFrom.DISK);
            Matrix matrix = new Matrix();
            matrix.postRotate(orientation);
            Bitmap bitmapRotated = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
            if(!bm.isRecycled()) bm.recycle();

//            Bitmap bm = MediaStore.Video.Thumbnails.getThumbnail(context.getContentResolver(), Long.parseLong(data.uri.getPath()), MediaStore.Video.Thumbnails.MINI_KIND, null);
//            Bitmap bm = ThumbnailUtils.createVideoThumbnail(data.uri.getPath(), MediaStore.Images.Thumbnails.MINI_KIND);
            return new Result(bitmapRotated, Picasso.LoadedFrom.DISK);
        }
    }
}
