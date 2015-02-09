package me.smartco.akstore.store.mongodb.mall.promotion;

/**
 * Created by libin on 14-11-27.
 */
public class Fact {

    private Object obj;

    public Fact(Object obj) {
        this.obj=obj;
    }

    public Condition on(Condition condition) throws NoSuchFieldException, IllegalAccessException {
        return condition.eval(this);
    }

    public Object getObj() {
        return obj;
    }
}
