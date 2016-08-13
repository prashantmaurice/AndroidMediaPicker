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

import com.prashantmaurice.android.mediapicker.ExternalInterface.CustomFolder;
import com.prashantmaurice.android.mediapicker.MediaPicker;
import com.prashantmaurice.android.mediapicker.Models.FolderObj;
import com.prashantmaurice.android.mediapicker.Models.MImageObj;
import com.prashantmaurice.android.mediapicker.Models.MLocalFolderObj;
import com.prashantmaurice.android.mediapicker.Models.MVideoObj;
import com.prashantmaurice.android.mediapicker.R;
import com.prashantmaurice.android.mediapicker.Utils.Logg;

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
public class VideoListFragment extends Fragment implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
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

        Uri u = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Video.VideoColumns.DATA,
                MediaStore.Video.Media._ID,
                MediaStore.Video.VideoColumns.LATITUDE,
                MediaStore.Video.VideoColumns.WIDTH,
                MediaStore.Video.VideoColumns.HEIGHT,
                MediaStore.Video.VideoColumns.LONGITUDE,//Orientation is always giving 0
                MediaStore.Video.VideoColumns.DURATION,
                MediaStore.Video.VideoColumns.DESCRIPTION,
                MediaStore.Video.VideoColumns.DATE_TAKEN
        };
        return new CursorLoader(getActivity(), u, projection, null, null, MediaStore.Video.VideoColumns.DATE_TAKEN + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor c) {
        Logg.d(TAG,"onLoadFinished");

        Map<String, MLocalFolderObj> folders = new HashMap<>();

        c.moveToFirst();


        while (!c.isAfterLast()) {
            String fullPath = c.getString(c.getColumnIndex(MediaStore.Video.VideoColumns.DATA));// /storage/emulated/0/TinyStep/TinyStep Images/IMG 1439401529025.jpg
            if(fullPath==null) fullPath = "";
            String tempDir = fullPath.substring(0, fullPath.lastIndexOf("/"));

            if (!folders.containsKey(tempDir)) {
                MLocalFolderObj mLocalFolderObj = new MLocalFolderObj(tempDir, MediaPicker.Pick.VIDEO);

                //Set am imageObj as latest one
                long id = c.getLong(c.getColumnIndex(MediaStore.Video.Media._ID));
                long dateTaken = c.getLong(c.getColumnIndex(MediaStore.Video.Media.DATE_TAKEN));
                double lat = c.getDouble(c.getColumnIndex(MediaStore.Video.Media.LATITUDE));
                double longg = c.getDouble(c.getColumnIndex(MediaStore.Video.Media.LONGITUDE));
                int duration = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.DURATION));
                int width = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.WIDTH));
                int height = c.getInt(c.getColumnIndex(MediaStore.Video.VideoColumns.HEIGHT));
                String desc = c.getString(c.getColumnIndex(MediaStore.Video.Media.DESCRIPTION));
                MVideoObj mVideoObj = MVideoObj.Builder.generateFromMediaVideoCursor(id, dateTaken, width, height, lat, longg, desc, duration, 0);

                mLocalFolderObj.setLatestMediaObj(mVideoObj);


                folders.put(tempDir, mLocalFolderObj);
            }

            MLocalFolderObj MLocalFolderObj = folders.get(tempDir);
            MLocalFolderObj.setItemCount(MLocalFolderObj.getItemCount() + 1);
            c.moveToNext();
        }

        List<FolderObj> resultIAV = new ArrayList<>();
        Iterator<MLocalFolderObj> i = folders.values().iterator();
        MLocalFolderObj next;
        while(i.hasNext()){
            next = i.next();
            resultIAV.add(next);
        }

        //add custom folders
        for(CustomFolder customFolder : folderActivity.getConfiguration().getCustomFolders()){
            resultIAV.add(customFolder);
        }

        setFolderData(resultIAV);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Logg.d(TAG,"onLoaderReset");
    }
}

