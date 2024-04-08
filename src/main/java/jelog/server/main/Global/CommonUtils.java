package jelog.server.main.Global;

import jelog.server.main.Enum.OsEnum;

import java.util.Locale;

/**
 * Description :
 * PackageName : jelog.server.main.Global
 * FileName : CommonUtils
 * Author : User
 * TimeDate : 2024-04-08
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-04-08               jyMin                최초생성
 * ------------------------------------------------------------
 */
public class CommonUtils {
    public static OsEnum detectOS(String userAgent){
        if(userAgent == null) return OsEnum.OP_None;
        userAgent = userAgent.toLowerCase();

        if(userAgent.contains("windows")){ return OsEnum.OP_Windows; }
        else if (userAgent.contains("ios")) { return OsEnum.OP_IOS; }
        else if (userAgent.contains("android") || userAgent.contains("mobile")) { return OsEnum.OP_Mobile; }
        else { return OsEnum.OP_None; }
    }
}
