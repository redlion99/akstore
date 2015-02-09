package me.smartco.akstore.store.mongodb.mall.promotion;

/**
 * Created by libin on 14-11-16.
 */
public class Condition {

    private Boolean result;

    private String property;

    private OPTYPE op;

    private Double right;

    private Fact fact;

    private Boolean joinWithEnd=true;

    public Condition(String property, OPTYPE op, Double right, Boolean joinWithEnd) {
        this.property = property;
        this.op = op;
        this.right = right;
        this.joinWithEnd = joinWithEnd;
    }

    protected Condition eval(Fact fact) throws NoSuchFieldException, IllegalAccessException {
        Object value=fact.getObj().getClass().getDeclaredField(property).get(fact.getObj());
        Double left=Double.valueOf(String.valueOf(value));

        if(OPTYPE.greaterThan.equals(op)){
            result=(left>right);
        } else if(OPTYPE.lessThan.equals(op)){
            result=(left<right);
        } else if(OPTYPE.equal.equals(op)){
            result=(left==right);
        }
        this.fact=fact;
        return this;
    }

    public Condition concat(Condition condition) throws NoSuchFieldException, IllegalAccessException {
        if(joinWithEnd)
            result=(result&&condition.eval(fact).getResult());
        else
            result=(result||condition.eval(fact).getResult());
        return this;
    }

    enum OPTYPE{greaterThan,lessThan,equal}

    public Boolean getResult() {
        return result;
    }
}
