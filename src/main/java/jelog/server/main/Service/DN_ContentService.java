package jelog.server.main.Service;

import jelog.server.main.Model.DN_Content;
import jelog.server.main.Repositories.DN_CommentRepositories;
import jelog.server.main.Repositories.DN_ContentRepositories;
import jelog.server.main.Repositories.DN_FilesRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_ContentService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 */

@Slf4j
@Service
public class DN_ContentService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Autowired
    private DN_FilesRepositories dn_filesRepositories;
    @Autowired
    private DN_CommentRepositories dn_commentRepositories;
    @Autowired
    private DN_ContentRepositories dn_contentRepositories;

    /**
     * [Page] Main Content Page
     * */
    public Page<?> findPage(Pageable pageable, String Title){
        return dn_contentRepositories.findDN_ContentByContentTitleContainsOrderByContentIdDesc(Title, pageable);
    }

    /**
     * Main Content Detail
     * */
    public Optional<DN_Content> findContent(int mainNumber){
        return dn_contentRepositories.findByContentId(mainNumber);
    }

}
