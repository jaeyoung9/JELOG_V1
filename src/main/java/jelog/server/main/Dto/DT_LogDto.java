package jelog.server.main.Dto;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import jelog.server.main.Model.DN_LogModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDateTime;

/**
 * Description :
 * PackageName : jelog.server.main.Dto
 * FileName : DT_LogDto
 * Author : User
 * TimeDate : 2023-07-20
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-20               User                최초생성
 * ------------------------------------------------------------
 */

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DT_LogDto {

    private int dnLid;
    private OsEnum dnOs;
    private String dseSignID;
    private String queryString;
    private LocalDateTime inDate;

    /* Table -> DTO **/
    public DT_LogDto(final DN_LogModel dataTo){
        this.dnLid = dataTo.getDnLid();
        this.dnOs = dataTo.getDnOs();
        this.dseSignID = dataTo.getDseSignID();
        this.queryString = dataTo.getQueryString();
        this.inDate = dataTo.getInDate();
    }
    /* DTO ->  Table **/
    public static DN_LogModel dnLogModel(final DT_LogDto dto){
        return DN_LogModel.builder()
                .dnLid(dto.getDnLid())
                .dnOs(dto.getDnOs())
                .dseSignID(dto.getDseSignID())
                .queryString(dto.getQueryString())
                .inDate(dto.getInDate())
                .build();
    }
}
