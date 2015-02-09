package me.smartco.akstore.exception;

/**
 * Created by libin on 14-12-24.
 */
public class ShopTooFarException extends Exception {
    public ShopTooFarException(String shopName) {
        super(shopName);
    }
}
