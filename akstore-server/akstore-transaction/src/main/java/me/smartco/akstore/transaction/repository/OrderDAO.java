package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.OrderEntity;
import me.smartco.akstore.transaction.model.OrderGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

/**
 * Created by libin on 15-1-9.
 */
public interface OrderDAO extends JpaRepository<OrderEntity,String> {

    public Page<OrderEntity> findByCustomerId(String customer, Pageable pageable);

    public Page<OrderEntity> findByCustomerIdAndStatusGreaterThan(String customer,int status, Pageable pageable);

    public Page<OrderEntity> findByCustomerIdAndStatusLessThan(String customer,int status, Pageable pageable);


    public List<OrderEntity> findByOrderGroup(OrderGroup orderGroup);

    public Page<OrderEntity> findByShopId(String shop,Pageable page);
    public Page<OrderEntity> findByPartnerId(String partner,Pageable page);
    public Page<OrderEntity> findByShopIdAndCreateTimeBetween(String shop,Date dateStart,Date dateEnd,Pageable page);
    public Page<OrderEntity> findByShopIdAndCreateTimeGreaterThan(String shop,Date dateStart,Pageable page);
    public Page<OrderEntity> findByShopIdAndCreateTimeLessThan(String shop,Date dateEnd,Pageable page);

    public List<OrderEntity> findByShopIdAndCreateTimeGreaterThan(String shop,Date dateStart);
    public List<OrderEntity> findByShopIdAndStatusAndCreateTimeGreaterThan(String shop,int status,Date dateStart);

    public Page<OrderEntity>  findByStatusAndUpdateTimeLessThan(int status,Date dateStart,Pageable page);


}
