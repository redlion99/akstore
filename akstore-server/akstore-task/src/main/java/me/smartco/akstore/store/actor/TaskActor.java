package me.smartco.akstore.store.actor;

import akka.actor.ActorSelection;
import akka.actor.Props;
import akka.actor.UntypedActor;
import me.smartco.akstore.biz.conf.Configuration;
import me.smartco.akstore.transaction.service.TransactionService;
import me.smartco.akstore.common.event.order.*;
import me.smartco.akstore.common.model.OrderStatus;
import me.smartco.akstore.common.model.PaymentType;
import me.smartco.akstore.store.service.MallService;
import me.smartco.akstore.store.message.AggCommand;
import me.smartco.akstore.store.message.BuildDispatchOrder;
import me.smartco.akstore.common.model.Account;
import me.smartco.akstore.store.mongodb.mall.*;
import me.smartco.akstore.biz.spring.SpringUtil;
import me.smartco.akstore.transaction.model.BillFlow;
import me.smartco.akstore.transaction.model.OrderEntity;

import java.math.BigDecimal;

/**
 * Created by libin on 14-11-20.
 */
public class TaskActor extends UntypedActor {


    private MallService mallManager;
    private TransactionService transactionService;
    private Account primaryAccount;
    private Account shippingFeeAccount;
    private Account promotionAccount;
    private java.math.BigDecimal rebate_rate=BigDecimal.valueOf(0.10);

    public TaskActor() {
        SpringUtil springUtil = SpringUtil.getInstance();
        Configuration configuration = springUtil.getBean(Configuration.class);
        primaryAccount = configuration.getPrimaryAccount();
        shippingFeeAccount = configuration.getShippingFeeAccount();
        promotionAccount=configuration.getPromotionAccount();
        mallManager = springUtil.getBean(MallService.class);
        transactionService=springUtil.getBean(TransactionService.class);
        getContext().actorOf(Props.create(BillFlowActor.class), "bill");
        context().actorOf(Props.create(DispatchOrderBuilderActor.class),"dispatch");
        context().actorOf(Props.create(AggActor.class),"agg");
        getContext().actorSelection("akka.tcp://akkalk@127.0.0.1:2551/user/push");
    }

    @Override
    public void onReceive(Object message) throws Exception {
        if (message instanceof CreatedEvent) {
            String orderId = ((OrderEvent) message).getOrderId();
            OrderEntity order = transactionService.getOrderDAO().findOne(orderId);
            if (PaymentType.cash.equals(order.getPaymentType()) && OrderStatus.created==order.getStatus()) {
//                mallManager.assignDeliverTask(order);
            }

        } else if (message instanceof PayedEvent) {
            String orderId = ((OrderEvent) message).getOrderId();
            OrderEntity order = transactionService.getOrderDAO().findOne(orderId);
            if (!PaymentType.cash.equals(order.getPaymentType()) && OrderStatus.payed==order.getStatus()) {
                // >primaryAccount <primaryAccount >shippingFeeAccount
//                billFlow(order.toString(), order, primaryAccount, order.getTotal());
//
//                if (order.getShippingFee().doubleValue() > 0) {
//                    billFlow(order.toString(), order, primaryAccount, order.getShippingFee().multiply(BigDecimal.valueOf(-1)));
//                    billFlow(order.toString(), order, shippingFeeAccount, order.getShippingFee());
//                }
//                mallManager.assignDeliverTask(order);
            }


        } else if (message instanceof DeliveredEvent) {
            String orderId = ((DeliveredEvent) message).getOrderId();
            String staffId = ((DeliveredEvent) message).getStaffId();
            OrderEntity order = transactionService.getOrderDAO().findOne(orderId);

//            for (LineItem lineItem : order.lineItems()) {
//                Account account = lineItem.product().shop().partner().account();
//                // <shopaccount
//                billFlow(lineItem.toString(), order, account, order.getTotal().multiply(BigDecimal.valueOf(-1)));
//            }
//            for (Discount discount : order.getDiscountSet()) {
//                Account account = discount.account();
//                // >promotionacount
//                billFlow(discount.getName(), order, account, discount.getAmount());
//            }
//            if (PaymentType.cash.equals(order.getPaymentType()) && Status.delivered==order.getStatus()) {
//                Staff staff = mallManager.getStaffRepository().findOne(staffId);
//                Account account = staff.getAccount();
//                // >staffaccount
//                billFlow(order.toString(), order, account, order.getTotal());
//                if (order.getShippingFee().doubleValue() > 0) {
//                    //<primaryAccount >shippingFeeAccount
//                    billFlow(order.toString(), order, primaryAccount, order.getShippingFee().multiply(BigDecimal.valueOf(-1)));
//                    billFlow(order.toString(), order, shippingFeeAccount, order.getShippingFee());
//                }
//            }
        }else if (message instanceof CompletedEvent){
            String orderId = ((CompletedEvent) message).getOrderId();
            OrderEntity orderEntity = transactionService.getOrderDAO().findOne(orderId);
            for(me.smartco.akstore.transaction.model.LineItem lineItem:orderEntity.lineItems()){
                if(null!=lineItem.getRefereeId()){
                    Referee referee=mallManager.getReferee(lineItem.getRefereeId());
                    Account account=referee.account();
                    BigDecimal rebate=lineItem.getTotal().multiply(rebate_rate);
                    billFlow(orderEntity.getName(), orderEntity, account, rebate);
                    billFlow(orderEntity.getName(),orderEntity,promotionAccount,rebate);
                }
            }

        }else if(message instanceof BuildDispatchOrder){
            getActor("dispatch").forward(message,context());
        } else if(message instanceof AggCommand){
            getActor("agg").forward(message,context());
        }
    }

    private ActorSelection getActor(String path){
        return context().actorSelection(path);
    }

    private void billFlow(String name, OrderEntity order, Account account, BigDecimal amount) {
        BillFlow billFlow = new BillFlow(name, account.getId(), amount);
        billFlow.setOrder(order);
        getActor("bill").tell(billFlow,getSelf());
    }


}
