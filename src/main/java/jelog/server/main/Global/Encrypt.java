package jelog.server.main.Global;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Description :
 * PackageName : jelog.server.main.Global
 * FileName : Encrypt
 * Author : MinJaeYoung
 * TimeDate : 2023-07-26
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-26               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Component
public class Encrypt {


    /**
     * getSalt 생성
     * @return Salt
     */
    public static String getSalt(){

        // Random, salt add
        SecureRandom a = new SecureRandom();
        byte[] s = new byte[20];

        a.nextBytes(s);

        StringBuffer b = new StringBuffer();
        for(byte f : s){
            b.append(String.format("%02x", f));
        }

        return b.toString();
    }

    /**
     * getEncrypt SHA-256
     * @param a Password
     * @param s Salt
     * @return Password Enc
     */
    public static String getEncrypt(String a, String s){
        String result = "";
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");

            m.update((a + s).getBytes());
            byte[] pwdSalt = m.digest();

            StringBuffer sb = new StringBuffer();
            for(byte b : pwdSalt){
                sb.append(String.format("%02x",b));
            }
            result = sb.toString();

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
