package jelog.server.main.Service;

import jelog.server.main.Repositories.DN_CommentRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_CommentService
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
public class DN_CommentService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    @Autowired
    private DN_CommentRepositories dn_commentRepositories;
}
