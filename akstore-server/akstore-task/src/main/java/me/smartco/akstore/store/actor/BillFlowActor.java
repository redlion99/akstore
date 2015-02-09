package me.smartco.akstore.store.actor;

import akka.actor.UntypedActor;
import me.smartco.akstore.biz.service.TransactionService;
import me.smartco.akstore.biz.spring.SpringUtil;
import me.smartco.akstore.transaction.model.BillFlow;

/**
 * Created by libin on 14-11-20.
 */
public class BillFlowActor extends UntypedActor {
    private TransactionService transactionService;

    public BillFlowActor() {
        SpringUtil springUtil = SpringUtil.getInstance();
        this.transactionService = springUtil.getBean(TransactionService.class);
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof BillFlow) {
            BillFlow billFlow=(BillFlow)message;
            transactionService.calculateBalanceAndSaveBillFlow(billFlow);
        }
    }
}
