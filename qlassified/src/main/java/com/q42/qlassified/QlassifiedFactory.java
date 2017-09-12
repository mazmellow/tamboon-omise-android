package com.q42.qlassified;

import android.content.Context;

import com.q42.qlassified.Entry.EncryptedEntry;
import com.q42.qlassified.Entry.QlassifiedBoolean;
import com.q42.qlassified.Entry.QlassifiedEntry;
import com.q42.qlassified.Entry.QlassifiedFloat;
import com.q42.qlassified.Entry.QlassifiedInteger;
import com.q42.qlassified.Entry.QlassifiedLong;
import com.q42.qlassified.Entry.QlassifiedSerializable;
import com.q42.qlassified.Entry.QlassifiedString;
import com.q42.qlassified.Provider.QlassifiedGuard;
import com.q42.qlassified.Provider.QlassifiedKeyStore;
import com.q42.qlassified.Provider.QlassifiedSecurity;
import com.q42.qlassified.Storage.QlassifiedStorageService;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class QlassifiedFactory {

    private Context context;
    private QlassifiedSecurity keyStore;
    private QlassifiedStorageService storageService;

    public void create(Context context) {
        this.context = context;
        getKeyStore(context);
    }

    public boolean forceCreateKey(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                // New Qlassified object
                this.keyStore = new QlassifiedKeyStore(context);
                QlassifiedKeyStore qk = (QlassifiedKeyStore)this.keyStore;
                // Create keys
                qk.createKeys();
            } else {
                this.keyStore = new QlassifiedGuard();
                // Because the app is NOT supported for lower than Jelly bean -> Abandon this case
            }
        } catch (KeyStoreException |
                CertificateException |
                IOException |
                InvalidKeyException |
                NoSuchProviderException |
                InvalidAlgorithmParameterException |
                NoSuchAlgorithmException e) {
            Utility.logError("Qlassified", String.format("The Keystore could not be created. Stacktrace: %s", e.getMessage()));
            return false;
        }
        return true;
    }

    private boolean getKeyStore(Context context) {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
                this.keyStore = new QlassifiedKeyStore(context);
            } else {
                this.keyStore = new QlassifiedGuard();
            }
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            Utility.logError("Qlassified", String.format("The Keystore could not be created. Stacktrace: %s", e.getMessage()));
            return false;
        }
        return true;
    }

    public void setStorageService(QlassifiedStorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * We use a general method to put any type of object to the
     * storage, so we can perform general checks and insert
     * encrypted data into storage. The storage system does not
     * need to be aware of any type of value. It just gets a
     * string... deal with it!
     *
     * @param entry {QlassifiedEntry} The key that was used when the data was put
     * @return {boolean} Was the entry successfully encrypted and entered into the storage?
     */
    public boolean put(QlassifiedEntry entry) {

        if(!this.instanceCheck()) {
            return false;
        }

        EncryptedEntry encryptedEntry = keyStore.encryptEntry(entry);
        storageService.onSaveRequest(encryptedEntry);
        return true;
    }

    /**
     * We use a general method to fetch any type of object from
     * storage, so we can perform general checks and return the
     * arbitrary data. It's up to the method addressing this one to
     * verify the type of data returned.
     *
     * @param key {String} The key that was used when the data was put
     * @return Any gotten object from storage of any type
     */
    private Object get(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, NoSuchProviderException, InvalidKeyException {

        if(key == null || !this.instanceCheck()) {
            return null;
        }

        final EncryptedEntry encryptedEntry = storageService.onGetRequest(key);
        return keyStore.decryptEntry(encryptedEntry);
    }

    /**
     * All get methods returning the appropriate type
     */
    public Boolean getBoolean(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedBoolean entry = (QlassifiedBoolean) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Float getFloat(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedFloat entry = (QlassifiedFloat) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Integer getInt(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedInteger entry = (QlassifiedInteger) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Long getLong(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedLong entry = (QlassifiedLong) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public String getString(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedString entry = (QlassifiedString) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public Serializable getSerializable(String key) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, NoSuchProviderException, InvalidKeyException, BadPaddingException, NoSuchPaddingException, IllegalBlockSizeException {
        final QlassifiedSerializable entry = (QlassifiedSerializable) get(key);
        if(entry == null) {
            return null;
        }
        return entry.getValue();
    }

    public boolean instanceCheck() {
        return !(keyStore == null && !getKeyStore(context))
                && storageService != null;
    }
}
