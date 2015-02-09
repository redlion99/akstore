package me.smartco.akstore.store.mongodb.mall;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


/**
 * Created by libin on 15-1-7.
 */
public interface AdvertisementRepository extends MongoRepository<Advertisement,String> {

    public Page<Advertisement> findByActive(boolean active,Pageable pageable);
}
