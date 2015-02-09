package me.smartco.akstore.user.repository;

import me.smartco.akstore.user.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-10.
 */
public interface RoleRepository extends MongoRepository<Role, String> {
    public Role findOneByName(String name);
}
