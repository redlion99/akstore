package me.smartco.akstore.store.mongodb.mall;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

/**
 * Created by libin on 15-1-14.
 */
@Component
public interface RefereeRepository extends MongoRepository<Referee,String> {
}
