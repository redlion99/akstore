package me.smartco.akstore.store.mongodb.market;

import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

/**
 * Created by libin on 14-12-21.
 */
public interface DispatchOrderRepository extends MongoRepository<DispatchOrder,String> ,QueryDslPredicateExecutor<DispatchOrder> {
    public Page<DispatchOrder> findByShop(Shop shop,Pageable page);
    public Page<DispatchOrder> findByPartner(Partner partner,Pageable page);

    public DispatchOrder findOneByShop(Shop shop,Sort sort);
}
