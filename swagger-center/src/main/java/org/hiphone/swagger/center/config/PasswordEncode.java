package org.hiphone.swagger.center.config;

import org.hiphone.swagger.center.utils.EncryptoUtil;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author HiPhone
 */
public class PasswordEncode implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        return EncryptoUtil.encryptStringByBase64(charSequence.toString(), null);
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        return s.equals(encode(charSequence));
    }
}
