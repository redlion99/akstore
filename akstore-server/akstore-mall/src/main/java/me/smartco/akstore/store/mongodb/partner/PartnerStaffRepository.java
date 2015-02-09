package me.smartco.akstore.store.mongodb.partner;

import me.smartco.akstore.store.mongodb.core.user.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-12.
 */
public interface PartnerStaffRepository extends MongoRepository<PartnerStaff, String> {
    PartnerStaff findOneByUser(User user);
}
