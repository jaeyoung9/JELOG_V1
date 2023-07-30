package jelog.server.main.Model;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Enum.OsEnumConverter;
import jelog.server.main.Model.Jwt.Authority;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
@SequenceGenerator(name = "userSequence", sequenceName = "userSequence", initialValue = 237000)
public class DN_UserModel {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) /** 시퀀스 자동 생성*/
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
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

    @OneToMany(mappedBy = "dnUserModel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setDnUserModel(this));
    }
}
