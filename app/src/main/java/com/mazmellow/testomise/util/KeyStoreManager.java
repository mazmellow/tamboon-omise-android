package com.mazmellow.testomise.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import com.q42.qlassified.Qlassified;
import com.q42.qlassified.Storage.QlassifiedSharedPreferencesService;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import timber.log.Timber;

/**
 * Created by psc on 243//16.
 */
public class KeyStoreManager {


    private static final String AES_KEY = "secureValue00";
    private static final String AES_VECTOR = "secureValue01";

    public static final String OMISE_PKEY = "secureValue02";

    private static final String STORAGE_NAME = "Omise-Tamboon";
    private static Context applicationContext;

    /**
     * init KeyStoreManager
     * @param applicationContext should be application context. This can ensure the context validity
     */
    public static boolean init(Context applicationContext) {

        KeyStoreManager.applicationContext = applicationContext;

        // Init Qlassified service
        Qlassified.Service.start(applicationContext);
        Qlassified.Service.setStorageService(new QlassifiedSharedPreferencesService(applicationContext, STORAGE_NAME));  //Prepare Preferences

        return initAes();
    }

    private static boolean initAes() {

        //TODO: Init AES Keypair. Then encrypt with RSA key that keeped in Android Keystore System. Then save in Preferences with Base64.
        //TODO: Use this technic because AES is support encryption for a long length data (Long lenght data can't be stored in Android Keystore System).
        //TODO: AES key will be created only once (per installation). Because this process will take a time.

        SharedPreferences preferences = applicationContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        try {
            if (!preferences.contains(AES_KEY)) {
                Timber.d("Init AES_KEY");

                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(256);
                SecretKey secretKey = keyGen.generateKey();

                Qlassified.Service.put(AES_KEY, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
            }

            if (!preferences.contains(AES_VECTOR)) {
                Timber.d("Init AES_VECTOR");

                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                SecretKey secretKey = keyGen.generateKey();

                Qlassified.Service.put(AES_VECTOR, Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
            }
            return true;

        } catch (Exception e) {
            Timber.e("Init AES Error: "+e.getMessage());
            e.printStackTrace();

            return false;
        }
    }

    public static String getValue(String key) {
        String value = "";
        SharedPreferences preferences = applicationContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        if (preferences.contains(key)) value = decryptWithAes(preferences.getString(key, ""));
        return value;
    }

    public static void setValue(String key, String data) {
        SharedPreferences preferences = applicationContext.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, encryptWithAes(data));
        editor.commit();
    }


    private static String encryptWithAes(String plainText) {
        try {
            String aesKey = Qlassified.Service.getString(AES_KEY);
            String aesVec = Qlassified.Service.getString(AES_VECTOR);

            SecretKeySpec sKeySpec = new SecretKeySpec(Base64.decode(aesKey, Base64.DEFAULT), "AES");
            IvParameterSpec iv = new IvParameterSpec(Base64.decode(aesVec, Base64.DEFAULT));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, sKeySpec, iv);

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            return Base64.encodeToString(encrypted, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    private static String decryptWithAes(String cipherStr) {
        try {
            String aesKey = Qlassified.Service.getString(AES_KEY);
            String aesVec = Qlassified.Service.getString(AES_VECTOR);

            SecretKeySpec sKeySpec = new SecretKeySpec(Base64.decode(aesKey, Base64.DEFAULT), "AES");
            IvParameterSpec iv = new IvParameterSpec(Base64.decode(aesVec, Base64.DEFAULT));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, sKeySpec, iv);

            byte[] plainText = cipher.doFinal(Base64.decode(cipherStr, Base64.DEFAULT));

            return new String(plainText, "UTF-8");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}
