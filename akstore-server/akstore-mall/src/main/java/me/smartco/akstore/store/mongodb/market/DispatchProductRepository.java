package me.smartco.akstore.store.mongodb.market;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-12-21.
 */
public interface DispatchProductRepository extends MongoRepository<DispatchProduct,String> {

    public Page<DispatchProduct> findByActive(boolean active,Pageable pageable);

    public Page<DispatchProduct> findByActiveAndStockGreaterThan(boolean active,int stock,Pageable pageable);
}
