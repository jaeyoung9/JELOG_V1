package jelog.server.main.Repositories;


import jelog.server.main.Model.DN_UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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

    Optional<DN_UserModel> findByDaSignID(String daSignID);
    DN_UserModel findByDaSignIDAndDnPasswd(String daSignID, String dnPassword);
    boolean existsByDaSignID(String daSignID);
}
