package me.smartco.akstore.store.mongodb.mall;

import me.smartco.akstore.common.model.AbstractDocument;
import me.smartco.akstore.common.model.Location;
import me.smartco.akstore.common.model.Staff;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Created by libin on 14-11-16.
 */
@Document
public class DeliverTask extends AbstractDocument {

    @DBRef
    private Staff assignee;

    private Double[] location;

    private DeliverStatus status=DeliverStatus.created;



    public Staff getAssignee() {
        return assignee;
    }

    public void setAssignee(Staff assignee) {
        this.assignee = assignee;
    }


    public DeliverStatus getStatus() {
        return status;
    }

    public void setStatus(DeliverStatus status) {
        this.status = status;
    }

    public Location getLocation() {
        return Location.copy(location);
    }
    public void setLocation(Location loc) {
        this.location=new Double[]{loc.getLat(),loc.getLng()};
    }
}

