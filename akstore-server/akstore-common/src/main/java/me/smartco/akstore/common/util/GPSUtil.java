package me.smartco.akstore.common.util;


import me.smartco.akstore.common.model.Location;

/**
 * Created by libin on 14-12-24.
 */
public class GPSUtil {
    public static double distanceBetween(Location locationA, Location locationB)
    {
        double pk = (double)(180 / 3.14169);


        double a1 = locationA.getLat() / pk;
        double a2 = locationA.getLng() / pk;
        double b1 = locationB.getLat() / pk;
        double b2 = locationB.getLng() / pk;


        double t1 = Math.cos(a1) * Math.cos(a2) * Math.cos(b1) * Math.cos(b2);
        double t2 = Math.cos(a1) * Math.sin(a2) * Math.cos(b1) * Math.sin(b2);
        double t3 = Math.sin(a1) * Math.sin(b1);
        double tt = Math.acos(t1 + t2 + t3);


        return 6366000 * tt;
    }
}
