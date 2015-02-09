package me.smartco.akstore.common.event.order;

/**
 * Created by libin on 15-1-14.
 */
public class CompletedEvent extends OrderEvent{
    public CompletedEvent(String orderId) {
        super(orderId);
    }
}
