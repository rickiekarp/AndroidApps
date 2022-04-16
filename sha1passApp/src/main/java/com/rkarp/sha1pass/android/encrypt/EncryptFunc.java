package com.rkarp.sha1pass.android.encrypt;

import android.util.Log;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class EncryptFunc {

    public static String calcHMAC(String data, String key) throws SignatureException {

        final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

        try {
            // get an hmac_sha1 key from the raw key bytes
            SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);

            // get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            byte[] digest = mac.doFinal(data.getBytes());

            StringBuilder sb = new StringBuilder(digest.length*2);
            String s;
            for (byte b : digest){
                s = Integer.toHexString(0xFF & b);
                sb.append(s);
            }
            s = sb.toString();

            return s;

        } catch (Exception e) {
            throw new SignatureException("Failed to generate HMAC : " + e.getMessage());
        }
    }

    public static byte[] getSha1(String p) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            Log.e("SHA1Pass", "npe", e);
        }
        try {
            md.update(p.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            Log.e("SHA1Pass", "error utf-8", e);
        }
        return md.digest();
    }

    public static String bytesToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();
        for (byte aData : data) {
            int halfbyte = (aData >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                if ((0 <= halfbyte) && (halfbyte <= 9))
                    buf.append((char) ('0' + halfbyte));
                else
                    buf.append((char) ('a' + (halfbyte - 10)));
                halfbyte = aData & 0x0F;
            } while (two_halfs++ < 1);
        }
        return buf.toString();
    }
}
