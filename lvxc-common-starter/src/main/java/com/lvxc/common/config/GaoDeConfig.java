package com.lvxc.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spatial4j.core.context.SpatialContext;
import com.spatial4j.core.distance.DistanceUtils;
import com.spatial4j.core.shape.Rectangle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
public class GaoDeConfig {

    @Value("${gaode.key:#{'eb957ff5c4b1b5a9e046b48b0a16a9a0'}}")
    private String key;

    @Value("${gaode.getLonAndLatUrl:#{'http://restapi.amap.com/v3/geocode/geo'}}")
    private String getLonAndLatUrl;

    @Value("${gaode.getAddressUrl:#{'http://restapi.amap.com/v3/geocode/regeo'}}")
    private String getAddressUrl;

    @Value("${gaode.getDistanceUrl:#{'http://restapi.amap.com/v3/distance'}}")
    private String getDistanceUrl;

    /**
     * 地址转换为经纬度
     *
     * @param address 地址
     * @return 经纬度
     */
    public Map<String, String> getLonAndLat(String address) {
        // 返回输入地址address的经纬度信息, 格式是 经度,纬度
        String queryUrl = getLonAndLatUrl + "?key=" + key + "&address=" + address;
        // 高德接口返回的是JSON格式的字符串
        String queryResult = getResponse(queryUrl);
        Map<String, String> map = new HashMap<>();
        JSONObject obj = JSONObject.parseObject(queryResult);
        if (ObjectUtil.isNotEmpty(obj) && ObjectUtil.isNotEmpty(obj.get("status"))
                && obj.get("status").toString().equals("1")) {
            JSONObject jobJSON = JSONObject.parseObject(obj.get("geocodes").toString().substring(1, obj.get("geocodes").toString().length() - 1));
            String location = jobJSON.get("location").toString();
            log.info("经纬度：" + location);
            String[] lonAndLat = location.split(",");
            if (lonAndLat != null && lonAndLat.length == 2) {
                map.put("lng", lonAndLat[0]);
                map.put("lat", lonAndLat[1]);
            }
            return map;
        } else {
            throw new RuntimeException("地址转换经纬度失败，错误码：" + obj.get("infocode"));
        }
    }

    /**
     * 将经纬度getLng， getLat 通过getAMapByLngAndLat方法转换地址
     *
     * @param getLng 经度
     * @param getLat 纬度
     * @return 地址名称
     * @throws Exception
     */
    public String getAMapByLngAndLat(String getLng, String getLat) {
        String url;
        try {
            url = getAddressUrl + "?output=JSON&location=" + getLng + ","
                    + getLat + "&key=" + key + "&radius=0&extensions=base";
            String queryResult = getResponse(url); // 高德接品返回的是JSON格式的字符串
            // 将获取结果转为json数据
            JSONObject obj = JSONObject.parseObject(queryResult);
            log.info("obj为：" + obj);
            if (ObjectUtil.isNotEmpty(obj) && ObjectUtil.isNotEmpty(obj.get("status"))
                    && obj.get("status").toString().equals("1")) {
                // 如果没有返回-1
                JSONObject regeocode = obj.getJSONObject("regeocode");
                if (regeocode.size() > 0) {
                    // 在regeocode中拿到 formatted_address 具体位置
                    return regeocode.get("formatted_address").toString();
                } else {
                    throw new RuntimeException("未找到相匹配的地址！");
                }
            } else {
                throw new RuntimeException("请求错误！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "-1";
    }

    /**
     * 根据两个定位点的经纬度算出两点间的距离
     *
     * @param startLonLat 起始经纬度
     * @param endLonLat   结束经纬度（目标经纬度）
     * @return 两个定位点之间的距离
     */
    private long getDistance(String startLonLat, String endLonLat) {
        // 返回起始地startAddr与目的地endAddr之间的距离，单位：米
        Long result;
        String queryUrl = getDistanceUrl + "?key=" + key + "&origins=" + startLonLat + "&destination=" + endLonLat;
        String queryResult = getResponse(queryUrl);
        JSONObject obj = JSONObject.parseObject(queryResult);
        if (ObjectUtil.isNotEmpty(obj)) {
            JSONArray ja = obj.getJSONArray("results");
            if (ObjectUtil.isNotEmpty(ja)) {
                JSONObject jobO = JSONObject.parseObject(ja.getString(0));
                if (ObjectUtil.isNotEmpty(jobO)) {
                    result = Long.parseLong(jobO.get("distance").toString());
                } else {
                    throw new RuntimeException("请求错误！");
                }
            } else {
                throw new RuntimeException("请求错误！");
            }
        } else {
            throw new RuntimeException("请求错误！");
        }
        log.info("距离：" + result);
        return result;
    }

    /**
     * 将计算得到的 最大最小经纬度放入条件中，查出来的数据使用 getDistance 方法过滤
     * 正方形, 根据中心点计算出经纬度最大值（左上角，右下角坐标）
     *
     * @param radius    半径
     * @param CenterLng 中心点 经度
     * @param CentraLat 中心点 纬度
     * @return
     */
    private Rectangle getRectangle(double radius, double CenterLng, double CentraLat) {
        //// 如果字段类型为 geo_point，可以直接使用(test没的权限上传，没得用在删除)
        //        /*GeoDistanceQueryBuilder distanceQueryBuilder = new GeoDistanceQueryBuilder("location");
        //        distanceQueryBuilder
        //                .point(companyPageQuery.getCentrePointLatitude(), companyPageQuery.getCentrePointLongitude())
        //                .distance(companyPageQuery.getRadius(), DistanceUnit.KILOMETERS);
        //        boolQueryBuilder.filter(distanceQueryBuilder);*/
        SpatialContext spatialContext = SpatialContext.GEO;
        return spatialContext.getDistCalc()
                .calcBoxByDistFromPt(spatialContext.makePoint(CenterLng, CentraLat),
                        radius * DistanceUtils.KM_TO_DEG, spatialContext, null);
    }


    /**
     * 圆形，剔除半径超过指定距离的多余地点
     * 其实就是 计算两个经纬度之间的距离
     *
     * @param equGislong
     * @param equGislat
     * @param longitude
     * @param latitude
     * @return
     */
    private double getDistance(Double equGislong, Double equGislat, double longitude, double latitude) {
        SpatialContext spatialContext = SpatialContext.GEO;
//        System.out.println(Math.ceil(spatialContext.calcDistance(spatialContext.makePoint(longitude, latitude),
//                spatialContext.makePoint(equGislong, equGislat)) * DistanceUtils.DEG_TO_KM));
        return Math.ceil(spatialContext.calcDistance(spatialContext.makePoint(longitude, latitude),
                spatialContext.makePoint(equGislong, equGislat)) * DistanceUtils.DEG_TO_KM);
    }


    /**
     * 发送请求
     *
     * @param serverUrl 请求地址
     */
    private static String getResponse(String serverUrl) {
        // 用JAVA发起http请求，并返回json格式的结果
        StringBuffer result = new StringBuffer();
        try {
            URL url = new URL(serverUrl);
            URLConnection conn = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result.toString();
    }


}
