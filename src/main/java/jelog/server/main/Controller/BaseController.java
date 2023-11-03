package jelog.server.main.Controller;

import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
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

}
