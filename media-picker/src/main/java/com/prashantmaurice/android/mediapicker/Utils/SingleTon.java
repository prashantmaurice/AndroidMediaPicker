package com.prashantmaurice.android.mediapicker.Utils;

import com.prashantmaurice.android.mediapicker.ExternalInterface.Configuration;

public class SingleTon {

    private static SingleTon sInstance;

    //SingleTon Controllers
    DataController dataController;
    SelectionController selectionController;
    Configuration configuration;

    SingleTon(Configuration configuration){
        Logg.d("MainApplication","onCreate");
        sInstance = this;

        //Initialize controllers
        this.configuration = configuration;
        dataController = DataController.getInstance();
        selectionController = SelectionController.getInstance();
        selectionController.reset();
    }

    public synchronized static SingleTon recreateInstance(Configuration configuration) {
        if(sInstance==null) sInstance = new SingleTon(configuration);
        else{
            sInstance.reset();
            sInstance.configuration = configuration;
        }
        return sInstance;
    }

    public synchronized static SingleTon getInstance() {
        return sInstance;
    }

    /** Main Getter functions to access these controllers */
    public SelectionController getSelectionController(){
        return selectionController;
    }
    public DataController getDataController(){
        return dataController;
    }
    public Configuration getConfiguration(){
        return this.configuration;
    }


    public void reset() {
        selectionController.reset();
    }
}
