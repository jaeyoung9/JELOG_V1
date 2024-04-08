package jelog.server.main.Service;

import jelog.server.main.Repositories.DN_LogRepositories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description :
 * PackageName : jelog.server.main.Service
 * FileName : DN_LogService
 * Author : MinJaeYoung
 * TimeDate : 2023-07-20
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2023-07-20               MinJaeYoung                최초생성
 * ------------------------------------------------------------
 * 2024-04-08               jyMin                @Autowired 수정
 */

@Slf4j
@Service
public class DN_LogService {

    /**
     * [Variables]
     * Repository
     * */
    //-------------------------------------------------------------------------------------------------------------------------------------

    private final DN_LogRepositories dn_logRepositories;
    @Autowired
    public DN_LogService(DN_LogRepositories dn_logRepositories){
        this.dn_logRepositories = dn_logRepositories;
    }

}
