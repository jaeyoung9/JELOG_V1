package jelog.server.main.Config;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.Encrypt;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Model.Jwt.Authority;
import jelog.server.main.Repositories.DN_UserRepositories;
import org.junit.platform.commons.logging.Logger;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * Description :
 * PackageName : jelog.server.main.Config
 * FileName : DatabaseSeeder
 * Author : User
 * TimeDate : 2024-04-05
 * ============================================================
 * DATE                      AUTHOR                      NOTE
 * ------------------------------------------------------------
 * 2024-04-05               jyMin                최초생성
 * ------------------------------------------------------------
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final DN_UserRepositories userRepositories;
    public DatabaseSeeder(DN_UserRepositories userRepositories){
        this.userRepositories = userRepositories;
    }

    @Override
    @Transactional
    public void run(String[] args) throws Exception {

        try{
            // 계정 있는경우 생성안함
            if(userRepositories.existsByDaSignID("jyMin")){
                logger.info(() -> "Seeder skipped: An Admin account that has already been created.");
                return;
            }

            // 솔트 값 생성
            String Salt = Encrypt.getSalt();
            DN_UserModel dataUser = DN_UserModel.builder()
                    .daSignID("jyMin")
                    .dnName("관리자")
                    .dnPasswd(Encrypt.getEncrypt("971027", Salt))
                    .dnSalt(Salt)
                    .dnGuest(false)
                    .dnUserAuthEnum(OsEnum.OP_User2)
                    .build();

            Authority role = Authority.builder()
                    .name("ROLE_ADMIN")
                    .build();

            dataUser.setRoles(Collections.singletonList(role));
            role.setDnUserModel(dataUser);

            DN_UserModel savedUser = userRepositories.save(dataUser);
            logger.info(() -> "Seeder data success !!! : " + savedUser);
        }catch (Exception e){
            logger.error(e, () -> "Seeder Data ERROR !!! : " + e.getMessage());
        }

    }
}
