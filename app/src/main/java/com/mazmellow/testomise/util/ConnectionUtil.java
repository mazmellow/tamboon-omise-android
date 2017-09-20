package com.mazmellow.testomise.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import timber.log.Timber;

/**
 * Created by maz on 9/19/2017 AD.
 */

public class ConnectionUtil {

    public static boolean hasInternet(Context context) {

        boolean hasInternet = true;

        if (context == null) hasInternet = false;

        ConnectivityManager conMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo i = conMgr.getActiveNetworkInfo();
        if (i == null || !i.isConnected() || !i.isAvailable()) hasInternet = false;

        if (!hasInternet) Timber.w(" -------- NO INTERNET ------- ");

        return hasInternet;
    }
}
