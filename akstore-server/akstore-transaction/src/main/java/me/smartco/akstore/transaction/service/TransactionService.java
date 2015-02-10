package me.smartco.akstore.transaction.service;

import me.smartco.akstore.transaction.model.*;
import me.smartco.akstore.transaction.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by libin on 15-1-9.
 */
@Service
@Transactional(readOnly = true)
public class TransactionService  {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private OrderGroupDAO orderGroupDAO;

    @Autowired
    private OrderHistoryDAO orderHistoryDAO;


    @Autowired
    private LineItemDAO lineItemDAO;


    @Autowired
    private BillFlowDAO billFlowRepository;

    @Autowired
    private BillDAO billDAO;

    public OrderDAO getOrderDAO() {
        return orderDAO;
    }

    public OrderGroupDAO getOrderGroupDAO() {
        return orderGroupDAO;
    }

    public OrderHistoryDAO getOrderHistoryDAO() {
        return orderHistoryDAO;
    }

    public LineItemDAO getLineItemDAO() {
        return lineItemDAO;
    }

    public BillFlowDAO getBillFlowRepository() {
        return billFlowRepository;
    }

    public BillDAO getBillDAO() {
        return billDAO;
    }

    private Logger logger= LoggerFactory.getLogger(TransactionService.class);


    public BigDecimal getAccountBalance(String accountId){
        Page<BillFlow> page=billFlowRepository.findByAccountId(accountId, new PageRequest(0, 1, Sort.Direction.DESC, "createTime"));
        if(page.hasContent()){
            return page.getContent().get(0).getBalance();
        }
        return BigDecimal.ZERO;
    }


    public synchronized BillFlow calculateBalanceAndSaveBillFlow(BillFlow billFlow){
        BigDecimal balance=getAccountBalance(billFlow.getAccountId());
        billFlow.setBalance(balance.add(billFlow.getAmount()));
        return billFlowRepository.save(billFlow);
    }


    public List aggOrdersBefore(Date time) {
        return lineItemDAO.aggOrdersBefore(time);
    }

    public Page<OrderEntity> findByStatusAndUpdateTimeLessThan(int delivered, Date time, PageRequest pageRequest) {
        return orderDAO.findByStatusAndUpdateTimeLessThan(delivered,time,pageRequest);
    }


    public List<OrderEntity> findByShopIdAndStatusAndCreateTimeGreaterThan(String id, int created, Date dateFrom) {
        return orderDAO.findByShopIdAndStatusAndCreateTimeGreaterThan(id,created,dateFrom);
    }
}


