package com.ipl.utility;

import android.app.Application;
import android.content.SharedPreferences;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * Created by bridgeit on 2/6/16.
 */

public class FirebaseApplication extends Application {

    private final String PREFS_NAME = "TopicSubcribe";
    private final String TOPIC_SUBCRIBEDE = "TOPIC_SUBCRIBEDE";
    private final String TOPIC_NAME = "2016";

    @Override
    public void onCreate() {
        super.onCreate();

        //Enabling Offline Capabilities
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //Retrieving shared preference
        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Reading from SharedPreferences
        boolean sharedPreferencesFlag = sharedPreferences.getBoolean(TOPIC_SUBCRIBEDE, false);

        if (!sharedPreferencesFlag) {
            //Subscribe To Topic
            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_NAME);

            // Writing data to SharedPreferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(TOPIC_SUBCRIBEDE, true);
            editor.commit();
        }
    }
}
