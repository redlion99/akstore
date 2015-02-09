package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by libin on 15-1-9.
 */
public interface OrderHistoryDAO extends JpaRepository<OrderHistory,String> {
}
