package me.smartco.akstore.transaction.repository;

import me.smartco.akstore.transaction.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by libin on 14-11-12.
 */
public interface BillDAO extends JpaRepository<Bill,String> {
    Page<Bill> findByAccountId(String accountId, Pageable page);
}
