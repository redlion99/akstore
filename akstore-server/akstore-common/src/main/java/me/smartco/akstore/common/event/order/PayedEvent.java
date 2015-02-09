package me.smartco.akstore.common.event.order;

/**
 * Created by libin on 14-11-20.
 */
public class PayedEvent extends OrderEvent {
    public PayedEvent(String orderId) {
        super(orderId);
    }
}
