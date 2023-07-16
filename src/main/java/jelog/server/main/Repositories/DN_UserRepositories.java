package jelog.server.main.Repositories;


import jelog.server.main.Model.DN_UserModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface DN_UserRepositories extends JpaRepository<DN_UserModel,Integer> {

    Optional<DN_UserModel> findByDaSignID(String daSignID);
    boolean existsByDaSignID(String daSignID);
}
