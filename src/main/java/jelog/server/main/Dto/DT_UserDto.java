package jelog.server.main.Dto;


import jelog.server.main.Model.DN_UserModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_UserDto {
    private int DnUid;
    private String DaSignID;
    private String DnPasswd;
    private String DnSalt;


    /* Table -> DTO **/
    public DT_UserDto(final DN_UserModel dataTo){
        this.DnUid = dataTo.getDnUid();
        this.DaSignID = dataTo.getDaSignID();
        this.DnPasswd = dataTo.getDnPasswd();
        this.DnSalt = dataTo.getDnSalt();
    }

    /* DTO ->  Table **/
    public static DN_UserModel DN_US_Entity(final DT_UserDto dto){
        return DN_UserModel.builder().DnUid(dto.getDnUid())
                .DaSignID(dto.getDaSignID()).DnPasswd(dto.getDnPasswd())
                .DnSalt(dto.getDnSalt()).build();
    }
}
