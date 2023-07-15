package jelog.server.main.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * Description :
 * PackageName : jelog.server.main.Enum
 * FileName : OsEnum
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-15               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Getter
public enum OsEnum {

    /**
     * [Enum]
     *
     * */
    //-----------------------------------------------------------
    OP_None("OP_None",0),
    OP_Windows("OP_Windows", 10000),
    OP_Mobile("OP_Mobile", 10001);

    //-----------------------------------------------------------




    /**
     * [OsEnum]
     * Variables
     * */
    //-----------------------------------------------------------
    private String title;
    private Integer titleCode;

    OsEnum(String title, Integer titleCode){
        this.title = title;
        this.titleCode = titleCode;
    }

    public static OsEnum LegacyCode(Integer titleCode){
        return Arrays.stream(OsEnum.values())
                .filter(f-> f.getTitleCode().equals(titleCode))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException(String.format("State Code : [%s] does not exist.", titleCode)));
    }

}

