package me.smartco.akstore.common.event.order;

/**
 * Created by libin on 14-11-20.
 */
public class CreatedEvent extends OrderEvent {

    public CreatedEvent(String orderId) {
        super(orderId);
    }
}
