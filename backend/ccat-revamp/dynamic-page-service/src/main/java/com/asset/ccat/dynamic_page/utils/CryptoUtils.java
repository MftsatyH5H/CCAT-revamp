package com.asset.ccat.dynamic_page.utils;

import com.asset.ccat.dynamic_page.defines.Defines;
import com.asset.ccat.dynamic_page.defines.ErrorCodes;
import com.asset.ccat.dynamic_page.exceptions.DynamicPageException;
import com.asset.ccat.dynamic_page.logger.CCATLogger;
import org.bouncycastle.util.encoders.UTF8;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;


@Component
public class CryptoUtils {

    private static final String ALGO = "AES"; // Default uses ECB PKCS5Padding

    public String encrypt(String Data, String secret) throws DynamicPageException {
        try {
            Key key = generateKey(secret);
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.ENCRYPT_MODE, key);
            byte[] encVal = c.doFinal(Data.getBytes());
            String encryptedValue = Base64.getEncoder().encodeToString(encVal);
            return encryptedValue;
        } catch (Exception ex) {
            CCATLogger.DEBUG_LOGGER.debug("An error occured during encryption, " + ex.getMessage());
            CCATLogger.DEBUG_LOGGER.error("An error occured during encryption", ex);
            throw new DynamicPageException(ErrorCodes.ERROR.ENCRYPTION_FAILED, Defines.SEVERITY.ERROR);
        }
    }

    public String decrypt(String strToDecrypt, String secret) throws DynamicPageException {
        try {
            Key key = generateKey(secret);
            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
        } catch (Exception ex) {
            CCATLogger.DEBUG_LOGGER.debug("An error occured during decryption, " + ex.getMessage());
            CCATLogger.DEBUG_LOGGER.error("An error occured during decryption", ex);
            throw new DynamicPageException(ErrorCodes.ERROR.DECRYPTION_FAILED, Defines.SEVERITY.ERROR);
        }
    }

    private Key generateKey(String secret) throws Exception {

        String encodedSeccret = new String(Base64.getEncoder().encode(
                secret.getBytes(StandardCharsets.UTF_8)));
        byte[] decoded = Base64.getDecoder().decode(encodedSeccret.getBytes());
        Key key;

        if (decoded.length < 16) {
            //Add padding if key less than 16
            byte[] decodedPadded = new byte[16];
            for (int i = 0; i < 16; i++) {
                if (i < decoded.length) {
                    decodedPadded[i] = decoded[i];
                } else {
                    decodedPadded[i] = 0;
                }
            }
            key = new SecretKeySpec(decodedPadded, ALGO);
        } else {
            key = new SecretKeySpec(decoded, ALGO);
        }
        return key;
    }

    public String decodeKey(String str) {
        byte[] decoded = Base64.getDecoder().decode(str.getBytes());
        return new String(decoded);
    }

    public String encodeKey(String str) {
        byte[] encoded = Base64.getEncoder().encode(str.getBytes());
        return new String(encoded);
    }

}
