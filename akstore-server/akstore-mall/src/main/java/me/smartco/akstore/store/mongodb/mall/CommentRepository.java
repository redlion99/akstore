package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.store.mongodb.partner.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by libin on 14-11-27.
 */
public interface CommentRepository extends MongoRepository<Comment,String> {

    public Page<Comment> findByShop(Shop shop,Pageable page);

    public Page<Comment> findByProduct(Product product,Pageable page);
}
