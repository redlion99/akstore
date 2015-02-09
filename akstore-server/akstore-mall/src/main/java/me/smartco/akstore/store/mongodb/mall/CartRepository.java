package me.smartco.akstore.store.mongodb.mall;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-15.
 */
public interface CartRepository extends MongoRepository<Cart,String> {

    public Cart findOneByCustomer(Customer customer);
}
