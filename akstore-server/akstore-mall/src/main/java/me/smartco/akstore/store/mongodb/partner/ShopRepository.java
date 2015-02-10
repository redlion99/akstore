package me.smartco.akstore.store.mongodb.partner;

import me.smartco.akstore.common.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by libin on 14-11-11.
 */
public interface ShopRepository extends MongoRepository<Shop,String>{

    public Page<Shop> findByNameLike(String name, Pageable pageable);
    public Page<Shop> findByPartner(Partner partner, Pageable pageable);
    public Page<Shop> findByLocationWithinAndActive(Circle circle,Boolean active, Pageable pageable);

    public Page<Shop> findByLocationNear(Point point, Pageable pageable);

    public Page<Shop> findByLocationWithin(Box box, Pageable pageable);

    public Shop findOneByAccount(Account account);

    public List<Shop> findByDispatchOptions(DispatchOptions dispatchOptions);


}
