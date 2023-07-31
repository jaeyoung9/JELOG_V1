package jelog.server.main.Repositories;

import jelog.server.main.Model.DN_Files;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_FilesRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Repository
public interface DN_FilesRepositories extends JpaRepository<DN_Files, Integer> {

    // 콘텐츠 Id로 파일 찾기 (단일)
    Optional<DN_Files> findByContentId(Integer contentId);

    // 콘텐츠 Id로 저장 파일 모두 찾기
    Optional<List<DN_Files>> findAllByContentId(Integer contentId);

}
