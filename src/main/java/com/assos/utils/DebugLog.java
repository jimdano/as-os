package com.assos.utils;

import android.util.Log;

/**
 * Created by jimmy on 18/06/2014
 */
public class DebugLog {
    public static void log(String logMessage) {
        if(Configuration.DEBUG_MODE) {
            Log.v(Configuration.DEBUG_TAG, logMessage);
        }
    }
}
