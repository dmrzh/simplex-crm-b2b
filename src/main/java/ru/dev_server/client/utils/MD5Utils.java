package ru.dev_server.client.utils;

import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**.*/
public class MD5Utils {
    private static final Logger LOG = LoggerFactory.getLogger(MD5Utils.class);

    public static String calculateSignature(String str) {
        byte[] bytes = str.getBytes();
        MessageDigest md5 = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            LOG.error(e.getMessage(),e);
            return null;
        }
        byte[] digest = md5.digest(bytes);

        return Hex.encodeHexString(digest);
    }
}
