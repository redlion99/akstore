package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.store.mongodb.partner.Partner;
import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Box;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import java.util.List;

/**
 * Created by libin on 14-11-7.
 */
public interface ProductRepository extends MongoRepository<Product,String> {

    /**
     * Returns a {@link Page} of {@link Product}s having a description which contains the given snippet.
     *
     * @param description
     * @param pageable
     * @return
     */
    Page<Product> findByDescriptionContaining(String description, Pageable pageable);

    /**
     * Returns all {@link Product}s having the given attribute.
     *
     * @param key
     * @return
     */
    @Query("{ ?0 : ?1 }")
    List<Product> findByAttributes(String key, String value);

    public Page<Product> findByShop(Shop shop, Pageable pageable);

    public Page<Product> findByShopAndActiveAndFeature(Shop shop,Boolean Active,Boolean feature, Pageable pageable);

    public Page<Product> findByShopAndActive(Shop shop,Boolean Active, Pageable pageable);

    public Page<Product> findByActive(Boolean active, Pageable pageable);

    public Page<Product> findByActiveAndLocationWithin(Boolean Active,Circle circle, Pageable pageable);

    public Page<Product> findByActiveAndFeatureAndLocationWithin(Boolean Active,Boolean feature,Circle circle, Pageable pageable);

    public Page<Product> findByActiveAndLocationNear(Boolean Active,Point point, Pageable pageable);

    public Page<Product> findByActiveAndLocationWithin(Boolean Active,Box box, Pageable pageable);


    @Query("{\"active\":true,\"location\" : {\"$within\" : {\"$center\" : [ [?0, ?1], ?2]}}}")
    public Page<Product> findActiveByLocationWithin(Double lat,Double lng,Double distance, Pageable pageable);



    public Page<Product> findByPartnerAndNameLike(Partner partner, String name, Pageable pageable);

}
