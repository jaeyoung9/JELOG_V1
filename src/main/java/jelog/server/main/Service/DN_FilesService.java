package jelog.server.main.Service;

import jelog.server.main.Repositories.DN_FilesRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_FilesService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-31
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-31               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2024-04-08               jyMin                @Autowired 수정
 */

@Slf4j
@Service
public class DN_FilesService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------
    private final DN_FilesRepositories dn_filesRepositories;

    @Autowired
    public DN_FilesService(DN_FilesRepositories dn_filesRepositories){
        this.dn_filesRepositories = dn_filesRepositories;
    }
}
