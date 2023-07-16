package jelog.server.main.Dto;


import jelog.server.main.Model.DN_UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_UserDto {
    private int dnUid;
    private String daSignID;
    private String dnPasswd;
    private String dnSalt;


    /* Table -> DTO **/
    public DT_UserDto(final DN_UserModel dataTo){
        this.dnUid = dataTo.getDnUid();
        this.daSignID = dataTo.getDaSignID();
        this.dnPasswd = dataTo.getDnPasswd();
        this.dnSalt = dataTo.getDnSalt();
    }

    /* DTO ->  Table **/
    public static DN_UserModel dnUserEntity(final DT_UserDto dto){
        return DN_UserModel.builder().dnUid(dto.getDnUid())
                .daSignID(dto.getDaSignID()).dnPasswd(dto.getDnPasswd())
                .dnSalt(dto.getDnSalt()).build();
    }
}
