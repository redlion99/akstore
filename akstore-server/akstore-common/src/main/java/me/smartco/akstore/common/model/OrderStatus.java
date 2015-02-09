package me.smartco.akstore.common.model;

//import sun.jvm.hotspot.utilities.Assert;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by libin on 14-12-4.
 */
public class OrderStatus {

    public final static int created = 1;//000000000001  0x001
    public final static int canceled = 1<<1| created;;//000000000011 0x003

    public final static int payed = 1<<4 | created;//000000010001  0x011
    public final static int refund = 1<<5 | payed|canceled;//000000110011 0x033

    public final static int assigned = 1<<8 | payed;//000100010001 0x111
    public final static int accepted = 1<<9 | assigned;//001100010001 0x311
    public final static int delivered = 1<<10 | accepted;//011100010001 0x711


    public final static int completed = 1<<2 | delivered;//011100010101 0x715

    public final static int returned = 1<<11 | delivered;//111100010001 0xf11
    public final static int closed=1<<3 |returned|refund;//111100111011 0xf3b

    public final static Map<Integer,String> textMap=new LinkedHashMap<Integer, String>();

    static {
        textMap.put(1,"新订单");
        textMap.put(3,"已取消");
        textMap.put(17,"已支付");
        textMap.put(51,"已退款");
        textMap.put(273,"正在出库");
        textMap.put(785,"正在送货");
        textMap.put(1809,"已送达");
        textMap.put(1813,"已完成");
        textMap.put(3857,"已退货");
        textMap.put(3899,"已关闭");
    }

    public static String text(int status){
        return textMap.get(status);
    }

    public static boolean hasFlag(int status,int flag){
        return (status&flag)==flag;
    }

    public static void main(String[] args){

        System.out.println( Integer.toHexString(created));
        System.out.println(Integer.toHexString(canceled));
        System.out.println(Integer.toHexString(payed));
        System.out.println(Integer.toHexString(refund));
        System.out.println(Integer.toHexString(assigned));
        System.out.println(Integer.toHexString(accepted));
        System.out.println(Integer.toHexString(delivered));


        System.out.println(Integer.toHexString(completed));
        System.out.println(Integer.toHexString(returned));
        System.out.println(Integer.toHexString(closed));



//        Assert.that(hasFlag(delivered, payed), "");
//        Assert.that(hasFlag(accepted, payed), "");
//        Assert.that(hasFlag(returned, payed), "");
//
//        Assert.that(hasFlag(payed,delivered)==false,"");
//        Assert.that(hasFlag(payed,accepted)==false,"");
//        Assert.that(hasFlag(canceled,payed)==false,"");


    }

}


