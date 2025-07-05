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
import javax.validation.constraints.*;
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
@Table(name = "users", indexes = {
    @Index(name = "idx_user_signin_id", columnList = "daSignID"),
    @Index(name = "idx_user_email", columnList = "email"),
    @Index(name = "idx_user_status", columnList = "status"),
    @Index(name = "idx_user_created", columnList = "inDate")
})
@SequenceGenerator(name = "userSequence", sequenceName = "userSequence", initialValue = 237000)
public class DN_UserModel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequence")
    @Column(name = "dnUid")
    private int dnUid;

    @Convert(converter = OsEnumConverter.class)
    @Column(name = "dnUserAuthEnum")
    private OsEnum dnUserAuthEnum;

    @NotBlank(message = "로그인 ID는 필수입니다.")
    @Size(min = 4, max = 50, message = "로그인 ID는 4자 이상 50자 이하로 입력해주세요.")
    @Column(name = "daSignID", unique = true, nullable = false, length = 50)
    private String daSignID;

    @NotBlank(message = "이름은 필수입니다.")
    @Size(min = 1, max = 100, message = "이름은 1자 이상 100자 이하로 입력해주세요.")
    @Column(name = "dnName", nullable = false, length = 100)
    private String dnName;
    
    @Column(name = "dnPasswd", nullable = false)
    private String dnPasswd;
    
    @Column(name = "dnSalt")
    private String dnSalt;
    
    @Column(name = "dnGuest", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean dnGuest = false;
    
    @Column(name = "dnGuestQuestion", length = 200)
    private String dnGuestQuestion;
    
    @Column(name = "dnGuestAnswer", length = 200)
    private String dnGuestAnswer;
    
    // New enhanced fields
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Column(name = "email", unique = true, length = 100)
    private String email;
    
    @Column(name = "nickname", length = 50)
    private String nickname;
    
    @Column(name = "profileImage", length = 255)
    private String profileImage;
    
    @Column(name = "bio", length = 500)
    private String bio;
    
    @Column(name = "website", length = 255)
    private String website;
    
    @Column(name = "location", length = 100)
    private String location;
    
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, INACTIVE, BANNED, PENDING
    
    @Column(name = "emailVerified", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean emailVerified = false;
    
    @Column(name = "emailVerificationToken", length = 100)
    private String emailVerificationToken;
    
    @Column(name = "passwordResetToken", length = 100)
    private String passwordResetToken;
    
    @Column(name = "passwordResetExpiry")
    private LocalDateTime passwordResetExpiry;
    
    @Column(name = "lastLoginDate")
    private LocalDateTime lastLoginDate;
    
    @Column(name = "loginAttempts", columnDefinition = "INT DEFAULT 0")
    @Builder.Default
    private int loginAttempts = 0;
    
    @Column(name = "accountLocked", columnDefinition = "BOOLEAN DEFAULT FALSE")
    @Builder.Default
    private boolean accountLocked = false;
    
    @Column(name = "lockTime")
    private LocalDateTime lockTime;

    // 사용자 등록, 수정일
    @CreationTimestamp
    @Column(name = "inDate", nullable = false, updatable = false)
    private LocalDateTime inDate;

    @UpdateTimestamp
    @Column(name = "update_date", nullable = false)
    private LocalDateTime upDate;

    @OneToMany(mappedBy = "dnUserModel", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @Builder.Default
    private List<Authority> roles = new ArrayList<>();

    public void setRoles(List<Authority> role) {
        this.roles = role;
        role.forEach(o -> o.setDnUserModel(this));
    }
    
    // Helper methods
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }
    
    public boolean isAdmin() {
        return roles.stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getName()));
    }
    
    public boolean hasRole(String roleName) {
        return roles.stream().anyMatch(role -> roleName.equals(role.getName()));
    }
    
    public String getDisplayName() {
        return nickname != null && !nickname.trim().isEmpty() ? nickname : dnName;
    }
    
    public boolean isAccountNonLocked() {
        if (!accountLocked) return true;
        if (lockTime == null) return false;
        return LocalDateTime.now().isAfter(lockTime.plusHours(24)); // Auto unlock after 24 hours
    }
    
    public void incrementLoginAttempts() {
        this.loginAttempts++;
        if (this.loginAttempts >= 5) {
            this.accountLocked = true;
            this.lockTime = LocalDateTime.now();
        }
    }
    
    public void resetLoginAttempts() {
        this.loginAttempts = 0;
        this.accountLocked = false;
        this.lockTime = null;
    }
}
