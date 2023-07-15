package jelog.server.main.Model;


import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.text.DateFormat;
import java.time.LocalDateTime;

/**
 *
 * TimeDate : 2023-07-03 21:01
 *
 * */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class DN_LogModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int DnLid;

    //@Enumerated(value = EnumType.STRING) 문자열 타입 사용
    //@Column(name = "DN_OS")
    @Convert(converter = OsEnumConverter.class)
    public OsEnum DnOs;

    public String UseSignID;
    public String QueryString;

    @CreationTimestamp
    public LocalDateTime inDate;
}
