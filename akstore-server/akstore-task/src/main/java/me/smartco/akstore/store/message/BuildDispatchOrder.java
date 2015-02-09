package me.smartco.akstore.store.message;

import me.smartco.akstore.store.mongodb.partner.DispatchOptions;

/**
 * Created by libin on 14-12-26.
 */
public class BuildDispatchOrder {

    private DispatchOptions dispatchOptions;

    public BuildDispatchOrder(DispatchOptions dispatchOptions) {
        this.dispatchOptions = dispatchOptions;
    }

    public DispatchOptions getDispatchOptions() {
        return dispatchOptions;
    }

    public void setDispatchOptions(DispatchOptions dispatchOptions) {
        this.dispatchOptions = dispatchOptions;
    }

    public static BuildDispatchOrder morning(){
        return new BuildDispatchOrder(new DispatchOptions(true,false,false));
    }
    public static BuildDispatchOrder noon(){
        return new BuildDispatchOrder(new DispatchOptions(false,true,false));
    }
    public static BuildDispatchOrder afternoon(){
        return new BuildDispatchOrder(new DispatchOptions(false,false,true));
    }


}
