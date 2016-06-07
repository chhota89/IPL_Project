package com.ipl.utility;

import android.content.Context;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by bridgeit on 4/6/16.
 */

public class FireBaseEvent {

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
}
