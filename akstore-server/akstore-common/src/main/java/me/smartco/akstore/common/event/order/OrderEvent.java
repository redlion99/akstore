package me.smartco.akstore.common.event.order;

import me.smartco.akstore.common.event.Message;
import me.smartco.akstore.common.model.OrderStatus;

import java.io.Serializable;

/**
 * Created by libin on 14-11-20.
 */
public class OrderEvent implements Message,Serializable {
    protected String orderId;


    public OrderEvent(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public static  OrderEvent create(int status,String orderId,String staffId){
        switch (status){
            case OrderStatus.created: return new CreatedEvent(orderId);
            case OrderStatus.payed: return new PayedEvent(orderId);
            case OrderStatus.delivered: return new DeliveredEvent(orderId,staffId);
            case OrderStatus.completed: return new CompletedEvent(orderId);
        }
        return null;
    }
}
