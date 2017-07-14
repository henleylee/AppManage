package com.liyunlong.appmanage.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5摘要辅助类
 *
 * @author liyunlong
 * @date 2017/4/19 13:41
 */
public class MD5Helper {

    public static String getMessageDigest(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(data);
        byte[] digest = messageDigest.digest();
        return byte2Hex(digest);
    }

    /**
     * 将byte数组转换成16进制
     *
     * @param buf
     * @return
     */
    private static String byte2Hex(byte[] buf) {
        int length = buf.length;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            buffer.append(hex);
        }
        return buffer.toString();
    }
}
