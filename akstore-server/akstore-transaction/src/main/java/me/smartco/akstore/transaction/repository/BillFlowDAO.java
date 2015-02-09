package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.BillFlow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;

/**
 * Created by libin on 14-11-12.
 */
public interface BillFlowDAO extends JpaRepository<BillFlow,String> {
    Page<BillFlow> findByAccountIdAndCreateTimeBetween(String accountId, Date startDate, Date endDate, Pageable page);
    Page<BillFlow> findByAccountId(String accountId, Pageable page);
}

