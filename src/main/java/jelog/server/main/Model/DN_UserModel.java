package jelog.server.main.Model;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Description :
 * PackageName : jelog.server.main.Model
 * FileName : DN_UserModel
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
public class DN_UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int dnUid;

    //@Enumerated(value = EnumType.STRING)
    @Convert(converter = OsEnumConverter.class)
    private OsEnum dnUserAuthEnum;

    private String daSignID;

    private String dnName;
    private String dnPasswd;
    private String dnSalt;
    private boolean dnGuest;
    private String dnGuestQuestion;
    private String dnGuestAnswer;

    // 사용자 등록, 수정일
    @CreationTimestamp
    public LocalDateTime inDate;
    @UpdateTimestamp
    public LocalDateTime upDate;
}
