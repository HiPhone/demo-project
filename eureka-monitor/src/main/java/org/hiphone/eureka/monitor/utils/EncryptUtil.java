package org.hiphone.eureka.monitor.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hiphone.eureka.monitor.constants.Constant;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author HiPhone
 */
@Slf4j
public class EncryptUtil {

    /**
     * 对称加密字符串
     * @param text 需要加密的字符串
     * @return 加密后的值
     */
    public static String encryptStringByBase64(String text) {
        byte[] decodedString = Base64.decodeBase64(Constant.BASE64_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedString, Constant.ENCRYPT_TYPE);

        String desString = null;

        try {
            Cipher cipher = Cipher.getInstance(Constant.ENCRYPT_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteText = text.getBytes(Constant.ENCODE_TYPE);
            byte[] cipherBytes = cipher.doFinal(byteText);
            desString = new String(Base64.encodeBase64(cipherBytes), Constant.ENCODE_TYPE);
        } catch (Exception e) {
            log.error("Encrypt gets error, please check it!");
        }
        return desString;
    }

    /**
     * 对称解密字符串
     * @param text 需要解密的字符串
     * @return 解密后的值
     */
    public static String decryptStringByBase64(String text) {
        byte[] decodedString = Base64.decodeBase64(Constant.BASE64_KEY);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedString, Constant.ENCRYPT_TYPE);

        String desString = null;

        try {
            Cipher cipher = Cipher.getInstance(Constant.ENCRYPT_TYPE);
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] byteText = Base64.decodeBase64(text);
            byte[] cipherBytes = cipher.doFinal(byteText);
            desString = new String(cipherBytes, Constant.ENCODE_TYPE);
        } catch (Exception e) {
            log.error("Decrypto gets error, please check it!");
        }
        return desString;
    }
}
