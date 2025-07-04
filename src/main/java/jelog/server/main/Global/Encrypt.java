package jelog.server.main.Global;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 패스워드 암호화 (BCrypt 사용)
     * @param password 평문 패스워드
     * @return 암호화된 패스워드
     */
    public static String encryptPassword(String password) {
        return passwordEncoder.encode(password);
    }

    /**
     * 패스워드 검증
     * @param rawPassword 평문 패스워드
     * @param encodedPassword 암호화된 패스워드
     * @return 검증 결과
     */
    public static boolean verifyPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * getSalt 생성 (레거시 호환을 위해 유지)
     * @return Salt
     * @deprecated BCrypt 사용으로 인해 더 이상 필요하지 않음
     */
    @Deprecated
    public static String getSalt(){
        SecureRandom a = new SecureRandom();
        byte[] s = new byte[20];
        a.nextBytes(s);
        StringBuilder b = new StringBuilder();
        for(byte f : s){
            b.append(String.format("%02x", f));
        }
        return b.toString();
    }

    /**
     * getEncrypt SHA-256 (레거시 호환을 위해 유지)
     * @param a Password
     * @param s Salt
     * @return Password Enc
     * @deprecated BCrypt 사용으로 인해 더 이상 권장하지 않음
     */
    @Deprecated
    public static String getEncrypt(String a, String s){
        String result = "";
        try {
            MessageDigest m = MessageDigest.getInstance("SHA-256");
            m.update((a + s).getBytes());
            byte[] pwdSalt = m.digest();
            StringBuilder sb = new StringBuilder();
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
