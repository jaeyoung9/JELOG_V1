package jelog.server.main.Repositories;

import jelog.server.main.Model.DN_LogModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_LogRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-20
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-20               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */
@Repository
public interface DN_LogRepositories extends JpaRepository<DN_LogModel, Integer> {
}
