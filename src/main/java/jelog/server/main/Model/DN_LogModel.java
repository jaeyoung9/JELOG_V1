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
 * 2024-04-08               MinJaeYoung                Object - dseIP 컬럼추가
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@SequenceGenerator(name = "logSequence", sequenceName = "logSequence", initialValue = 532932)
public class DN_LogModel {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "logSequence")
    private int dnLid;

    //@Enumerated(value = EnumType.STRING) //문자열 타입 사용
    //@Column(name = "DN_OS")
    @Convert(converter = OsEnumConverter.class)
    private OsEnum dnOs;

    private String dseSignID;
    private String dseIP;
    private String queryString;

    @CreationTimestamp
    private LocalDateTime inDate;
}

