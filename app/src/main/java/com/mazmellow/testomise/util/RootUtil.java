package com.mazmellow.testomise.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

// REF1: http://stackoverflow.com/questions/1101380/determine-if-running-on-a-rooted-device

/** @author Kevin Kowalewski */
public class RootUtil {

    public static boolean isDeviceRooted() {
        return checkRootMethod1() || checkRootMethod2() || checkRootMethod3();
    }

    /** @author Kevin Kowalewski */
    private static boolean checkRootMethod1() {
        String buildTags = android.os.Build.TAGS;
        return buildTags != null && buildTags.contains("test-keys");
    }

    /** @author Kevin Kowalewski */
    private static boolean checkRootMethod2() {
        String[] paths = { "/system/app/Superuser.apk", "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su" };
        for (String path : paths) {
            if (new File(path).exists()) return true;
        }
        return false;
    }

    /** @author Kevin Kowalewski */
    private static boolean checkRootMethod3() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[] { "/system/xbin/which", "su" });
            BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            if (in.readLine() != null) return true;
            return false;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }

}
