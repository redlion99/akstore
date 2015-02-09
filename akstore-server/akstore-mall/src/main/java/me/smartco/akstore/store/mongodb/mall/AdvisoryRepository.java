package me.smartco.akstore.store.mongodb.mall;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 15-1-8.
 */
public interface AdvisoryRepository extends MongoRepository<Advisory,String> {


}
