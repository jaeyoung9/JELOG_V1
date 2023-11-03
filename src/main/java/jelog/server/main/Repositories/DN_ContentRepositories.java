package jelog.server.main.Repositories;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Model.DN_Content;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Description :
 * PackageName : jelog.server.main.Repositories
 * FileName : DN_ContentRepositories
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Repository
public interface DN_ContentRepositories extends JpaRepository<DN_Content, Integer> {

    // Id 로 콘텐츠 찾기
    DN_Content findByContentId(Integer contentId);

    // 콘텐츠 전체 불러오기
    Optional<List<DN_Content>> findAllBy();

    // 콘텐츠 페이징
    Page<DN_Content> findDN_ContentByContentTitleContainsAndContentCategoriesInOrContentCategoriesIsNullOrderByContentIdDesc(String contentTitle, Set<OsEnum> categories, Pageable pageable);

}
