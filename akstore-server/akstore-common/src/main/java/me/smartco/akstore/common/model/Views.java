package me.smartco.akstore.common.model;

/**
 * Created by libin on 14-12-10.
 */
public class Views {
    public static interface View{}
    public static interface Brief extends View{}
    public static interface Detail extends Brief{}
    public static interface Full extends Detail{}
    public static interface Protected extends Detail{}
}
