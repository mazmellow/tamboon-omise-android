package com.mazmellow.testomise.util;

import android.text.TextUtils;

import java.nio.charset.Charset;

/**
 * Created by Maz on 4/27/2016 AD.
 */
public class StringUtil {
    public static byte[] toByteArray(String string) {
        if (TextUtils.isEmpty(string)) return new byte[]{};

        byte[] bytes = string.getBytes(Charset.forName("UTF-8"));
//        Timber.i("string = " + string);
//        Timber.i("bytes = "+ Arrays.toString(bytes));
//        Timber.i("-----------------");
        return bytes;
    }

    public static String bytesToString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) return null;

        String string = new String(bytes, Charset.forName("UTF-8"));
//        Timber.w("bytes = "+ Arrays.toString(bytes));
//        Timber.w("string = "+string);
//        Timber.w("-----------------");
        return string;
    }
}

