package com.prashantmaurice.android.mediapicker.Models;

/**
 * Created by maurice on 02/06/16.
 */
public class FolderObj {

    private String directory = "Dummy";
    private int itemCount = 0;


    public FolderObj(){}
    public FolderObj(String directory){
        this.directory = directory;
    }

    public String getName() {
        return ""+directory;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }
}
