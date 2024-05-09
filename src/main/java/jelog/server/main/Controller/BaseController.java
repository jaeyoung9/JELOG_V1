package jelog.server.main.Controller;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

/**
 * Description :
 * PackageName : jelog.server.main.Controller
 * FileName : BaseController
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 *
 * @RequestMapping Base Mapping
 */
@RestController
@RequestMapping(value = "/api")
//@Transactional(timeout = 1)
public class BaseController {

    /**
     * [Base] HttpServletRequest
     * @param m : Map<String,Object>  DTO Key and Value </String,Object>
     * @return HttpServletRequest to Use Rest API Request  Map Info Data Put HttpServletRequest to Conversion
     * */
    public HttpServletRequest serverServletRequest(Map<String, Object> m){

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        for(Map.Entry<String, Object> a : m.entrySet()){
            request.setAttribute(a.getKey(),a.getValue());
        }

        return request;
    }

    /**
     * [Base] string Array Add Value
     * @param originArray about Existing String Arrangement
     * @param value string To Add
     * @return newArray
     * */
    public String[] stringArrayAddValue(String[] originArray, String value){

        String[] newArray = new String[originArray.length + 1];

        for(int i=0; i < originArray.length; i++){
            newArray[i] = originArray[i];
        }
        newArray[originArray.length] = value;

        return newArray;
    }

    /**
     * [Base] string Array Remove
     * @param originArray about Existing String Arrangement
     * @param removeValue string To Remove
     * @return removeArray
     * */
    public String[] stringArrayRemoveValue(String[] originArray, String removeValue){

        String[] RemoveArray;
        RemoveArray = Arrays.stream(originArray)
                .filter(m -> !m.equals(removeValue))
                .toArray(String[]::new);

        return RemoveArray;
    }

    /**
     * [Base] string Array To Converting fo List
     * @param originArray array Of Strings To Be Converted
     * @param allowNullValue string Is a Add To List (Null Allowed)
     * @return newList
     * */
    public List<String> stringArrayConvertingList(String[] originArray, String allowNullValue){

        List<String> newList = new ArrayList<>(Arrays.asList(originArray));
        if(!allowNullValue.isBlank()) newList.add(allowNullValue);

        return  newList;
    }

    /**
     * [Base] remove Request Value After Converting String Array List
     * @param originArray array Of Strings To Be Converted
     * @param removeAllValue remove String (Null Not Allowed)
     * @return newList
     * */
    public List<String> stringArrayConvertingListRemoveAll(String[] originArray, String removeAllValue){

        List<String> newList = new ArrayList<>(Arrays.asList(originArray));
        newList.removeAll(Arrays.asList(removeAllValue));

        return newList;
    }

    /**
     * [Base] Random name
     * @return name
     * */
    public static String randomLetters(@Nullable int number){

        String result = "";
        if(number == 0){ number = 1; }

        for(int i=1; i<= number; i++) {
            int rnd = (int) (Math.random() * 52);
            char base = (rnd < 26 ? 'A' : 'a');
            char newChar = (char) (base + rnd % 26);
            result += String.valueOf(newChar);
        }

        return result;
    }

    /**
     * [Base] Token Decryption
     * @return boolean data
     * */
    public boolean quickTokenDecryption(String a){

        String[] chunks = a.split("\\.");

        Base64.Decoder decoder = Base64.getUrlDecoder();
//        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        boolean data = true;
        if(data == payload.contains("ROLE_USER") || data == payload.contains("ROLE_ADMIN")){
            return true;
        }else{
            return false;
        }
    }
    /**
     * [Base] hashCookieName
     * @return String data
     * */
    public String hashCookieName(String input) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : encodedhash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * [Base] HTML 엔티티 인코딩을 사용하여 XSS를 방지
     * @return String input
     * */
    public String encodeForHtml(String input) {
        if (input == null) {
            return null;
        }
        StringBuilder encoded = new StringBuilder(input.length());
        for (char c : input.toCharArray()) {
            switch (c) {
                case '&':
                    encoded.append("&amp;");
                    break;
                case '<':
                    encoded.append("&lt;");
                    break;
                case '>':
                    encoded.append("&gt;");
                    break;
                case '"':
                    encoded.append("&quot;");
                    break;
                case '\'':
                    encoded.append("&#x27;");
                    break;
                case '/':
                    encoded.append("&#x2F;");
                    break;
                default:
                    encoded.append(c);
            }
        }
        return encoded.toString();
    }
}
