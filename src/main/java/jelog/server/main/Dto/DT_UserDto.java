package jelog.server.main.Dto;


import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import jelog.server.main.Model.DN_UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_UserDto
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-19               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_UserDto {
    private int dnUid;

    private OsEnum dnUserAuthEnum;

    private String daSignID;
    private String dnName;
    private String dnPasswd;
    private String dnSalt;
    private boolean dnGuest;
    private String dnGuestQuestion;
    private String dnGuestAnswer;



    /* Table -> DTO **/
    public DT_UserDto(final DN_UserModel dataTo){
        this.dnUid = dataTo.getDnUid();
        this.dnName = dataTo.getDnName();
        this.dnUserAuthEnum = dataTo.getDnUserAuthEnum();
        this.daSignID = dataTo.getDaSignID();
        this.dnPasswd = dataTo.getDnPasswd();
        this.dnSalt = dataTo.getDnSalt();
        this.dnGuest = dataTo.isDnGuest();
        this.dnGuestQuestion = dataTo.getDnGuestQuestion();
        this.dnGuestAnswer = dataTo.getDnGuestAnswer();
    }

    /* DTO ->  Table **/
    public static DN_UserModel dnUserEntity(final DT_UserDto dto){
        return DN_UserModel.builder()
                .dnUid(dto.getDnUid())
                .dnUserAuthEnum(dto.getDnUserAuthEnum())
                .dnName(dto.getDnName())
                .daSignID(dto.getDaSignID())
                .dnPasswd(dto.getDnPasswd())
                .dnSalt(dto.getDnSalt())
                .dnGuest(dto.isDnGuest())
                .dnGuestQuestion(dto.getDnGuestQuestion())
                .dnGuestAnswer(dto.getDnGuestAnswer())
                .build();
    }
}
