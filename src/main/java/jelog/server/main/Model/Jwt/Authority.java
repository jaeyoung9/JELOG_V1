package jelog.server.main.Model.Jwt;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jelog.server.main.Model.DN_UserModel;
import lombok.*;

import javax.persistence.*;

/**
 * Description :
 * PackageName : jelog.server.main.Model.Jwt
 * FileName : Authority
 * Author : MinJaeYoung
 * TimeDate : 2023-07-26
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-26               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@SequenceGenerator(name = "authSequence", sequenceName = "authSequence", initialValue = 7354985)
public class Authority  {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "authSequence")
    @JsonIgnore
    private Long id;

    private String name;

    /**
     * @ManyToOne : User Table 1:N 관계
     * Example : dnUserModel = UserID PK.
     * ==================================
     * 1	ROLE_USER	237000
     * 2	ROLE_ADMIN	237000
     * 3	ROLE_USER	237001
     * 4	ROLE_USER	237002
     * ==================================
     * */
    @JoinColumn(name = "dnUserModel")
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private DN_UserModel dnUserModel;

    public void setDnUserModel(DN_UserModel dnUserModel) {
        this.dnUserModel = dnUserModel;
    }
}
