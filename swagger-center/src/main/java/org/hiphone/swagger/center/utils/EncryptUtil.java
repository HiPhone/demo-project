package org.hiphone.swagger.center.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;
import org.hiphone.swagger.center.constants.Constant;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * DES加密解密类
 * @author HiPhone
 */
@Slf4j
public class EncryptUtil {

    /**
     * 对称加密字符串
     * @param text 需要加密的字符串
     * @param base64Key 对称加密的key
     * @return 加密后的值
     */
    public static String encryptStringByBase64(String text, String base64Key) {
        if (StringUtils.isEmpty(base64Key)) {
            base64Key = Constant.BASE64_KEY;
        }
        byte[] decodedString = Base64.decodeBase64(base64Key);
        SecretKeySpec secretKeySpec = new SecretKeySpec(decodedString, Constant.ENCRYPT_TYPE);

        String desString = null;

        try {
            Cipher cipher = Cipher.getInstance(Constant.ENCRYPT_TYPE);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] byteText = text.getBytes(Constant.ENCODE_TYPE);
            byte[] cipherBytes = cipher.doFinal(byteText);
            desString = new String(Base64.encodeBase64(cipherBytes), Constant.ENCODE_TYPE);
        } catch (Exception e) {
            log.error("Encrypto gets error, please check it!");
        }
        return desString;
    }

    /**
     * 对称解密字符串
     * @param text 需要解密的字符串
     * @param base64key 对称加密的key
     * @return 解密后的值
     */
    public static String decryptStringByBase64(String text, String base64key) {
        if (StringUtils.isEmpty(base64key)) {
            base64key = Constant.BASE64_KEY;
        }
        byte[] decodedString = Base64.decodeBase64(base64key);
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
