package jelog.server.main.Model;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_LogModel
 * Author : MinJaeYoung
 * TimeDate : 2023-07-15
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-19               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class DN_LogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int dnLid;

    //@Enumerated(value = EnumType.STRING) //문자열 타입 사용
    //@Column(name = "DN_OS")
    @Convert(converter = OsEnumConverter.class)
    public OsEnum dnOs;

    public String dseSignID;
    public String queryString;

    @CreationTimestamp
    public LocalDateTime inDate;
}

