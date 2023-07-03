package jelog.server.main.Dto;


import jelog.server.main.Model.DN_UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_UserDto {
    private int DN_UID;
    private String DA_SignID;
    private String DN_Passwd;
    private String DN_Salt;


    /* Table -> DTO **/
    public DT_UserDto(final DN_UserModel dataTo){
        this.DN_UID = dataTo.getDN_UID();
        this.DA_SignID = dataTo.getDA_SignID();
        this.DN_Passwd = dataTo.getDN_Passwd();
        this.DN_Salt = dataTo.getDN_Salt();
    }

    /* DTO ->  Table **/
    public static DN_UserModel DN_US_Entity(final DT_UserDto dto){
        return DN_UserModel.builder().DN_UID(dto.getDN_UID())
                .DA_SignID(dto.getDA_SignID()).DN_Passwd(dto.getDN_Passwd())
                .DN_Salt(dto.getDN_Salt()).build();
    }
}
