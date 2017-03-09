package com.example.root.cloudnote;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by root on 10/3/17.
 */

public class CloudNote extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
