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
     * None : 0
     * User Os Enum : 10000 ~ 19999
     * User Type Enum : 20000 ~ 29999
     * User Board Enum : 40000 ~ 49999
     * User Category Level1 Enum : 50000 ~ 59999
     * User Category Level2 Enum : 60000 ~ 69999
     *
     * */
    //-----------------------------------------------------------
    OP_None("OP_None",0),
    OP_Windows("OP_Windows", 10000),
    OP_IOS("OP_IOS", 10001),
    OP_Mobile("OP_Mobile", 10002),

    //-----------------------------------------------------------
    OP_User0("OP_Guest", 20000),
    OP_User1("OP_User", 20344),
    OP_User2("OP_Admin", 24678),


    //-----------------------------------------------------------
    Other("Other_Categories", 49847),
    Java("Java_Categories",42958),
    JavaScript("JavaScript_Categories",42959),
    C("C_Categories",47685),
    Python("Python",45656),
    Shell("ShellScript_Categories",  48765),
    Security("Security_Categories", 41232),
    DeveloperCareerSkills("DeveloperCareerSkills_Categories",49999),


    //-----------------------------------------------------------
    Init("Next_None", 99999);
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

