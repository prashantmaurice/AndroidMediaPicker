package com.prashantmaurice.android.mediapicker.Utils;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;

/**
 * Created by maurice on 02/06/16.
 */
public class DataController {
    static DataController instance;
    private Configuration configuration;


    public static DataController getInstance(){
        if(instance==null) instance = new DataController();
        return instance;
    }

    DataController(){}

    public Configuration getConfiguration() {
        return configuration;
    }
    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void initialize(Configuration configuration) {
        this.configuration = configuration;
    }
}
