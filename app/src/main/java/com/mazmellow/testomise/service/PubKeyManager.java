package com.mazmellow.testomise.service;

import android.text.TextUtils;

import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPublicKey;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import timber.log.Timber;

/**
 * REF: https://www.owasp.org/index.php/Certificate_and_Public_Key_Pinning#Android
 */
public final class PubKeyManager implements X509TrustManager {

    private String pubKey;
    public PubKeyManager(String pubKey) {
        this.pubKey = pubKey;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }

    @Override
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        if (chain == null) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate array is null");
        }

        if (!(chain.length > 0)) {
            throw new IllegalArgumentException("checkServerTrusted: X509Certificate is empty");
        }

        if (!(null != authType && authType.toUpperCase().contains("RSA"))) {
            throw new CertificateException("checkServerTrusted: AuthType is not RSA");
        }

        // Hack ahead: BigInteger and toString(). We know a DER encoded Public Key begins
        // with 0x30 (ASN.1 SEQUENCE and CONSTRUCTED), so there is no leading 0x00 to drop.
        RSAPublicKey pubkey = (RSAPublicKey) chain[0].getPublicKey();
        String encoded = new BigInteger(1 /* positive */, pubkey.getEncoded()).toString(16);

        // Pin it!
        final boolean expected = pubKey == null || pubKey.equalsIgnoreCase(encoded);

        if (!expected) {
            throw new CertificateException("Public key doesn't matched!");
        }
    }

    public static SSLSocketFactory getSslSocketFactory(String pubKey) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{new PubKeyManager(pubKey)}, null);
            return sslContext.getSocketFactory();
        } catch (Exception e) {
            Timber.e("getSslSocketFactory() Error: %s" + e.toString());
        }
        return null;
    }

    public static HostnameVerifier getHostnameVerifier(final String host){
        return new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                Timber.d("getHostnameVerifier() -> hostname: "+hostname);

                if(TextUtils.isEmpty(hostname) || hostname.equalsIgnoreCase(host)){
                    return true;
                }else{
                    return false;
                }
            }
        };
    }
}