package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.OrderGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by libin on 15-1-9.
 */
public interface OrderGroupDAO extends JpaRepository<OrderGroup,String>{
}
