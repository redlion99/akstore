package me.smartco.akstore.common.event.order;

/**
 * Created by libin on 14-11-20.
 */
public class DeliveredEvent extends OrderEvent {
    private String staffId;

    public DeliveredEvent(String orderId, String staffId) {
        super(orderId);
        this.staffId = staffId;
    }

    public String getStaffId() {
        return staffId;
    }
}
