package com.projectpinacolada.ucsd.projectpinacolada;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by Austin on 3/3/2016.
 */
public class BeginningApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        // Add your initialization code here
        Parse.initialize(this);
    }
}
