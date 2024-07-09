package com.lvxc.common.utils;

import cn.hutool.core.lang.Assert;

/**
 * @ClassName LocationUtils
 * @Description 根据两地经纬度计算两地间距离
 * @Author hux
 * @Date 2024/4/1
 **/
public class LocationUtils {
    /**
     * 赤道半径
     */
    private static double EARTH_RADIUS = 6378.137;

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * @param origin 出发点经纬度，用","隔开
     * @param destination 目的地经纬度，用","隔开
     * @return double
     * @Author hux
     * @Description 通过经纬度获取距离(单位 ： 米)
     * @Date 2024/4/1
     **/
    public static double getDistance(String origin, String destination) {
        Assert.isTrue(origin != null, "出发点经纬度不可以为空！");
        Assert.isTrue(destination != null, "目的地经纬度不可以为空！");
        String[] temp = origin.split(",");
        String[] temp2 = destination.split(",");

        double radLat1 = rad(Double.parseDouble(temp[1]));
        double radLat2 = rad(Double.parseDouble(temp2[1]));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(temp[0])) - rad(Double.parseDouble(temp2[0]));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // 保留两位小数
        s = Math.round(s * 100d) / 100d;
        s = s * 1000;
        return s;
    }

    /**
     * @param originLon 出发点经度
     * @param originLat 出发点纬度
     * @param destinationLon 目的地经度
     * @param destinationLat 目的地经度
     * @return double
     * @Author hux
     * @Description 通过经纬度获取距离(单位 ： 米)
     * @Date 2024/4/1
     **/
    public static double getDistance(String originLon, String originLat, String destinationLon, String destinationLat) {
        Assert.isTrue(originLon != null, "出发点经度不可以为空！");
        Assert.isTrue(originLat != null, "出发点纬度不可以为空！");
        Assert.isTrue(destinationLon != null, "目的地经度不可以为空！");
        Assert.isTrue(destinationLat != null, "目的地纬度不可以为空！");

        double radLat1 = rad(Double.parseDouble(originLat));
        double radLat2 = rad(Double.parseDouble(destinationLat));
        double a = radLat1 - radLat2;
        double b = rad(Double.parseDouble(originLon)) - rad(Double.parseDouble(destinationLon));
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
                + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        // 保留两位小数
        s = Math.round(s * 100d) / 100d;
        s = s * 1000;
        return s;
    }

    public static void main(String[] args) {
        //测试：杭州市-上海市距离
        System.out.println(getDistance("120.209947,30.245853", "121.473701,31.230416"));
        System.out.println(getDistance("120.209947", "30.245853", "121.473701", "31.230416"));
    }
}
