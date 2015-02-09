package me.smartco.akstore.store.mongodb.mall;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

/**
 * Created by libin on 14-11-7.
 */
public interface CustomerRepository extends MongoRepository<Customer, String> {


   public Customer findByLoginname(@Param("loginname") String name);
}
