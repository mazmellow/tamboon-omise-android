package com.q42.qlassified.Provider;

import android.util.Base64;

import com.q42.qlassified.Utility;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class QlassifiedCrypto {

    public static final String CHARSET = "UTF8";
    public static final String ALGORITHM = "RSA/NONE/PKCS1Padding";
    public static final int BASE64_MODE = Base64.DEFAULT;

    public String encrypt(String input, RSAPublicKey publicKey) {

        if (input == null) {
            return null;
        }

        try {
            byte[] dataBytes = input.getBytes(CHARSET);
            Cipher cipher = Cipher.getInstance(ALGORITHM, new BouncyCastleProvider());
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            Utility.logDebug(Utility.LOG_TAG_CRYPTO, String.format("Encrypt Data's Lenght: %d", dataBytes.length));

            return Base64.encodeToString(cipher.doFinal(dataBytes), BASE64_MODE);
        }
        catch (IllegalBlockSizeException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt IllegalBlockSizeException: %s", e.getMessage()));
            return null;
        }
        catch (BadPaddingException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt BadPaddingException: %s", e.getMessage()));
            return null;
        }
        catch (NoSuchAlgorithmException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt NoSuchAlgorithmException: %s", e.getMessage()));
            return null;
        }
        catch (NoSuchPaddingException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt NoSuchPaddingException: %s", e.getMessage()));
            return null;
        }
        catch (UnsupportedEncodingException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt UnsupportedEncodingException: %s", e.getMessage()));
            return null;
        }
        catch (InvalidKeyException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Encrypt IllegalBlockSizeException: %s", e.getMessage()));
            return null;
        }
    }

    public String decrypt(String input, PrivateKey privateKey) throws NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, NoSuchProviderException, InvalidKeyException {

        if (input == null) {
            return null;
        }

        byte[] dataBytes = Base64.decode(input, BASE64_MODE);
        Cipher cipher;

        // Initialize cipher
        cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        try {
            Utility.logDebug(Utility.LOG_TAG_CRYPTO, String.format("Decrypt Data's Lenght: %d", dataBytes.length));

            return new String(cipher.doFinal(dataBytes));
        }
        catch (IllegalBlockSizeException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Decrypt IllegalBlockSizeException: %s", e.getMessage()));
            throw e;
        }
        catch (BadPaddingException e) {
            Utility.logError(Utility.LOG_TAG_CRYPTO, String.format("Decrypt BadPaddingException: %s", e.getMessage()));
            throw e;
        }
    }
}
