package com.ipl.utility;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.ipl.R;


/**
 * Created by bridgeit on 4/6/16.
 */

public class FireBaseEvent {

    private  static final String TAG = FireBaseEvent.class.getSimpleName();

    public static void sendImageDownloadEvent(Context context, String name, String category) {
        //Initialize analytics object.
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

        //Send Image Download event to the firebase analytics
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "Image Download");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, name);
        bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }

    public static void checkFireBaseRemoteConfig(){
        final FirebaseRemoteConfig firebaseRemoteConfig=FirebaseRemoteConfig.getInstance();

        FirebaseRemoteConfigSettings settings= new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build();

        firebaseRemoteConfig.setConfigSettings(settings);
        firebaseRemoteConfig.setDefaults(R.xml.defaults_config);

        firebaseRemoteConfig.fetch().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.d(TAG, "Fetch Succeeded");
                    // Once the config is successfully fetched it must be activated before newly fetched
                    // values are returned.
                    firebaseRemoteConfig.activateFetched();
                    Log.i(TAG, "onComplete: ............................"+firebaseRemoteConfig.getString("no_internet_connection"));
                } else {
                    Log.d(TAG, "Fetch failed");
                }
                /*TextView textView=(TextView)findViewById(R.id.textView);
                textView.setText(""+mFirebaseRemoteConfig.getString("my_country"));*/
            }
        });
    }


    /*//Sign in successfully
                        startActivityForResult(
                                AuthUI.getInstance()
                                        .createSignInIntentBuilder()
                                        .setTheme(R.style.GreenTheme)
                                        .setLogo(R.mipmap.ic_launcher)
                                        .setProviders(
                                                AuthUI.EMAIL_PROVIDER,
                                                AuthUI.GOOGLE_PROVIDER)
                                        .build(), RC_SIGN_IN);
*/
                        /*//sign out
                        AuthUI.getInstance()
                                .signOut(TeamView.this)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // user is now signed out
                                        Toast.makeText(TeamView.this,"Sign  out successfully",Toast.LENGTH_LONG).show();
                                    }
                                });*/

    /*protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(TeamView.this,"Sigin in successfully",Toast.LENGTH_LONG).show();
            } else {
                // user is not signed in. Maybe just wait for the user to press
                // "sign in" again, or show a message
            }
        }
    }*/

}
