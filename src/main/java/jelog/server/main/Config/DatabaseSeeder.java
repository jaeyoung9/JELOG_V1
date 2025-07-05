package jelog.server.main.Config;

import jelog.server.main.Enum.OsEnum;
import jelog.server.main.Global.Encrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import jelog.server.main.Model.DN_UserModel;
import jelog.server.main.Model.Jwt.Authority;
import jelog.server.main.Repositories.DN_UserRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
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
 * 2024-04-05               jyMin                       최초생성
 * ------------------------------------------------------------
 */

@Component
public class DatabaseSeeder implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final DN_UserRepositories userRepositories;
    private final PasswordEncoder passwordEncoder;
    
    public DatabaseSeeder(DN_UserRepositories userRepositories, PasswordEncoder passwordEncoder){
        this.userRepositories = userRepositories;
        this.passwordEncoder = passwordEncoder;
    }
    @Value("${object.model.id}")
    private String dataId;
    @Value("${object.model.pw}")
    private String dataPw;
    @Value("${object.model.adminRole}")
    private String dataRole;
    @Value("${object.model.adminRoleVal}")
    private String dataRoleVal;

    @Override
    @Transactional
    public void run(String[] args) {

        try{

            // 계정 있는경우 생성안함
            if(userRepositories.existsByDaSignID(dataId)){
                logger.info("Seeder skipped: An Admin account that has already been created.");
                return;
            }

            // BCrypt로 패스워드 암호화 (Salt는 더 이상 필요 없음)
            String encryptedPassword = passwordEncoder.encode(dataPw);
            DN_UserModel dataUser = DN_UserModel.builder()
                    .daSignID(dataId)
                    .dnName("관리자")
                    .dnPasswd(encryptedPassword)
                    .dnSalt(null) // BCrypt는 내부적으로 salt를 처리함
                    .dnGuest(false)
                    .dnUserAuthEnum(OsEnum.LegacyCode(Integer.parseInt(dataRole)))
                    .build();

            Authority role = Authority.builder()
                    .name(dataRoleVal)
                    .build();

            dataUser.setRoles(Collections.singletonList(role));
            role.setDnUserModel(dataUser);

            DN_UserModel savedUser = userRepositories.save(dataUser);
            logger.info("Seeder data success !!! : {}", savedUser);
        }catch (Exception e){
            logger.error("Seeder Data ERROR !!! : {}", e.getMessage(), e);
        }

    }
}
