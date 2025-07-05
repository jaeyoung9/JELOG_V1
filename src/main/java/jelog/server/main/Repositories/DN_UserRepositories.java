package jelog.server.main.Repositories;


import jelog.server.main.Model.DN_UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;



/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_UserRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-20
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-20               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Repository
public interface DN_UserRepositories extends JpaRepository<DN_UserModel,Integer> {

    // 사용자 데이터 불러오기
    Optional<DN_UserModel> findByDaSignID(String daSignID);

    // 로그인 유효성 검사 아이디 및 비밀번호 일치 여부 확인
    DN_UserModel findByDaSignIDAndDnPasswd(String daSignID, String dnPassword);

    // 아이디 Null 여부 판단
    boolean existsByDaSignID(String daSignID);
    
    // Enhanced methods for admin functionality
    
    // Search users by username or name
    Page<DN_UserModel> findByDaSignIDContainingIgnoreCaseOrDnNameContainingIgnoreCase(
        String signId, String name, Pageable pageable);
    
    // Get recent users (top 10)
    List<DN_UserModel> findTop10ByOrderByInDateDesc();
    
    // Find user by email
    Optional<DN_UserModel> findByEmail(String email);
    
    // Find user by password reset token
    Optional<DN_UserModel> findByPasswordResetToken(String token);
    
    // Find active users by status
    List<DN_UserModel> findByStatusOrderByLastLoginDateDesc(String status);
}
