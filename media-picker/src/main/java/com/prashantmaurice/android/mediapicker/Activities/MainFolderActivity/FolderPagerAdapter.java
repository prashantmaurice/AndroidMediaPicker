package com.prashantmaurice.android.mediapicker.Activities.MainFolderActivity;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.prashantmaurice.android.mediapicker.MediaPicker;

import java.util.List;

/**
 * Created by saikamisetti on 09/06/16.
 */
public class FolderPagerAdapter extends FragmentStatePagerAdapter {

    private FolderActivity activity;
    Context _context;
    LayoutInflater _inflater;
    LinearLayout _thumbnails;


    public FolderPagerAdapter(FragmentManager fm, FolderActivity addMediaActivity) {
        super(fm);
        this.activity = addMediaActivity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (activity.getConfiguration().getPick()){
            case IMAGE_VIDEO:
                if(position==0) return new ImageListFragment();
                else return new VideoListFragment();
            case IMAGE:
                return new ImageListFragment();
            case VIDEO:
                return new VideoListFragment();
            default:
                return new ImageListFragment();
        }
    }

    @Override
    public int getCount() {
        if(activity.getConfiguration().getPick().equals(MediaPicker.Pick.IMAGE_VIDEO)) return 2;
        return 1;
    }


    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;//for notify data set changed to work
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (activity.getConfiguration().getPick()){
            case IMAGE_VIDEO:
                if(position==0) return "IMAGES";
                else return "VIDEOS";
            case IMAGE:
                return "IMAGES";
            case VIDEO:
                return "VIDEOS";
            default:
                return "IMAGES";
        }
    }
}
