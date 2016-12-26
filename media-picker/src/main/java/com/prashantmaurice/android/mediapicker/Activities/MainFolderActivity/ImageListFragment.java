package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.prashantmaurice.android.mediapicker.ExternalInterface.CaptureImgFolder;
import com.prashantmaurice.android.mediapicker.ExternalInterface.CustomFolder;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;
import com.prashantmaurice.android.mediapicker.Utils.ToastMain2;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * Created by maurice on 09/06/16.
 *
 * you can also use this to decode
 * MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), alocalMediaObj.uri)
 */
public class ImageListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    static final String TAG = "ImageFoldersList";
    private FolderActivity folderActivity;

    GridView gridView;
    FolderActivityAdapter adapter;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        folderActivity = (FolderActivity) getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.frag_imagesfolder_list, container, false);
        gridView = (GridView) rootView.findViewById(R.id.gridView);
        initializeGridView(gridView);
        folderActivity.getSupportLoaderManager().initLoader((int)(Math.random()*2000), null, this);
        return rootView;
    }

    private void initializeGridView(GridView gridView) {
        adapter = new FolderActivityAdapter(folderActivity);
        gridView.setAdapter(adapter);
    }

    private void setFolderData(List<FolderObj> folders) {
        List<FolderObj> list = new ArrayList<>();
        list.addAll(folders);
        adapter.setData(list);
    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Logg.d(TAG,"onCreateLoader");

        Uri u = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Images.ImageColumns.DATA,
                MediaStore.Images.Media._ID,
                MediaStore.Images.ImageColumns.LATITUDE,
                MediaStore.Images.ImageColumns.WIDTH,
                MediaStore.Images.ImageColumns.HEIGHT,
                MediaStore.Images.ImageColumns.LONGITUDE,//Orientation is always giving 0
                MediaStore.Images.ImageColumns.ORIENTATION,
                MediaStore.Images.ImageColumns.DESCRIPTION,
                MediaStore.Images.ImageColumns.DATE_TAKEN
        };
        return new CursorLoader(getActivity(), u, projection, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        if(c==null){
            ToastMain2.showSmarterToast(folderActivity,"Error : Cursor is Empty","Error loading media");
            return;
        }

        Map<String, MLocalFolderObj> folders = new HashMap<>();

        c.moveToFirst();


        while (!c.isAfterLast()) {
            String fullPath = c.getString(c.getColumnIndex(MediaStore.Images.ImageColumns.DATA));// /storage/emulated/0/TinyStep/TinyStep Images/IMG 1439401529025.jpg
            if(fullPath==null) {
                c.moveToNext();
                continue;
            }
            String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

            if (!folders.containsKey(tempDir)) {
                MLocalFolderObj mLocalFolderObj = new MLocalFolderObj(tempDir, MediaPicker.Pick.IMAGE);

                //Set am imageObj as latest one//1453763584//79870665600000 //
                long id = c.getLong(c.getColumnIndex(MediaStore.Images.Media._ID));
                long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
                double lat = c.getDouble(c.getColumnIndex(MediaStore.Images.Media.LATITUDE));
                double longg = c.getDouble(c.getColumnIndex(MediaStore.Images.Media.LONGITUDE));
                int orientation = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.ORIENTATION));
                int width = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.WIDTH));
                int height = c.getInt(c.getColumnIndex(MediaStore.Images.ImageColumns.HEIGHT));
                String desc = c.getString(c.getColumnIndex(MediaStore.Images.Media.DESCRIPTION));

                //parse date-taken as in few phones it contained 79870665600000,
                if(dateTaken > System.currentTimeMillis()+(1000*60*60*24*1000)){
                    dateTaken = (new File(fullPath).lastModified());
                    if(dateTaken<10) dateTaken = System.currentTimeMillis();
                }

                MImageObj mImageObj = MImageObj.Builder.generateFromMediaImageCursor(id, dateTaken, width, height, lat, longg, desc, orientation, 0);
                mLocalFolderObj.setLatestMediaObj(mImageObj);


                folders.put(tempDir, mLocalFolderObj);
            }

            MLocalFolderObj MLocalFolderObj = folders.get(tempDir);
            MLocalFolderObj.setItemCount(MLocalFolderObj.getItemCount() + 1);
            c.moveToNext();
        }

        List<FolderObj> resultIAV = new ArrayList<>();

        if(FolderActivity.getConfiguration().getFrom().equals(MediaPicker.From.GALLERY_AND_CAMERA)) resultIAV.add(new CaptureImgFolder());

        Iterator<MLocalFolderObj> i = folders.values().iterator();
        MLocalFolderObj next;
        while(i.hasNext()){
            next = i.next();
            resultIAV.add(next);
        }

        //add custom folders
        for(CustomFolder customFolder : FolderActivity.getConfiguration().getCustomFolders()){
            resultIAV.add(customFolder);
        }

        setFolderData(resultIAV);
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Logg.d(TAG,"onLoaderReset");
    }
}

