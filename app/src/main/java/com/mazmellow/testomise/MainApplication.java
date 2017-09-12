package com.mazmellow.testomise;

import android.app.Application;

import com.mazmellow.testomise.util.KeyStoreManager;

import timber.log.Timber;

/**
 * Created by maz on 9/12/2017 AD.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        //TODO: Not Logging when Release app.
        if(BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        if(KeyStoreManager.init(this)){

            //TODO: Because hardcoding any keys in app is not secure. Best practice should get keys from server's API.
            //TODO: In this case, I assume that I get key from API and will save it in preference with encryption that related to Android Keystore System.
            String pKey = "pkey_test_599zublv8mxs6vg394a";
            KeyStoreManager.setValue(KeyStoreManager.OMISE_PKEY, pKey);
        }

        //TODO: Try to obfuscate urls and key to bytes. Then set them in Configuration.
//        StringUtil.toByteArray("https://tamboon-omise.herokuapp.com/");
//        StringUtil.toByteArray("tamboon-omise.herokuapp.com");
//        StringUtil.toByteArray("30820122300d06092a864886f70d01010105000382010f003082010a0282010100c9cdd6b1f839b62e6393476fec55e15577d19f9dc69912716156ca2e3237ce9e4ce03df40f4576e0b4d140e0b4cc1a0c3febe8b55fb65698d3298a29ae3e3b6e0f107e4caea47dd7dc89352da2fa5949d31407b6194ab16f461eecb278e8ba6692bb70f1ae6ad47e6798308c6c4fa0c132b75b33317f01519a6c1feffd489db3942d294e6dcfd3a40e65b910133890c2d3963e3ef6f396595061c271f6ffd5d623269c548ac9bca7d4c02199d6e94819e3ceebeb5d871496057d2ddb0879d2e09b244ec49483b02e273ecfc78e8000ab8627c0a857d03518702ad27805c2987b8bab502200314ac9e25c7cfde3f2606629a3e6b9f5d1d6d7536f138e95d075810203010001");
    }
}
