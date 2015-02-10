package me.smartco.akstore.store.mongodb.core;

import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.common.model.Staff;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;

/**
 * Created by libin on 14-11-12.
 */
public interface StaffRepository extends MongoRepository<Staff, String> {

    Staff findOneByAccount(Account account);


    List<Staff> findByLocationNearAndUpdatedAtGreaterThan(Point point,Date time);


}
