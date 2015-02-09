package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.LineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by libin on 15-1-9.
 */
public interface LineItemDAO extends JpaRepository<LineItem,String> {

    @Query(value="select sum(amount) as sold,productId from LineItem where createTime > ?1 group by productId", nativeQuery=true)
    public List aggOrdersBefore(Date dateFrom);
}
