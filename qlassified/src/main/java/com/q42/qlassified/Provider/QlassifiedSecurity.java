package com.q42.qlassified.Provider;

import com.q42.qlassified.Entry.QlassifiedEntry;
import com.q42.qlassified.Entry.EncryptedEntry;

import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableEntryException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public interface QlassifiedSecurity {

    EncryptedEntry encryptEntry(QlassifiedEntry classifiedEntry);
    QlassifiedEntry decryptEntry(EncryptedEntry entry) throws UnrecoverableEntryException, NoSuchAlgorithmException, KeyStoreException, InvalidKeyException, BadPaddingException, NoSuchProviderException, IllegalBlockSizeException, NoSuchPaddingException;
}
