package me.smartco.akstore.user.repository;

import me.smartco.akstore.user.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by libin on 14-11-7.
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByUsername(@Param("username") String name);

    User findByUsernameAndPasswordAndActive(@Param("username") String username, @Param("password") String password, @Param("active") Boolean active);

    User findByUsernameAndTokenAndActive(@Param("username") String username, @Param("token") String token, @Param("active") Boolean active);
    User findByTokenAndActive(@Param("token") String token, @Param("active") Boolean active);

}
