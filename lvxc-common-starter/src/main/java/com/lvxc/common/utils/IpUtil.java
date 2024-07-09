package com.lvxc.common.utils;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.lvxc.common.dto.AdminDistrictInfo;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName IpUtil
 * @Description IP工具类
 * @Author Roger
 * @Date 2023/9/10
 **/
@Slf4j
@Component
public class IpUtil {

    private static Searcher searcher;

    /**
     * 地址库查询
     *
     * @param ip 地址
     * @return java.lang.String
     * @version 1.0
     */
    public static AdminDistrictInfo getRealAddress(String ip) {
        if (internalIp(ip)) {
            return null;
        }
        AdminDistrictInfo result = new AdminDistrictInfo();
        try {
            // 加载地址库
            InputStream inputStream = new ClassPathResource("/config/ip2region.xdb").getInputStream();
            byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
            // 创建一个完全基于内存的查询对象
            searcher = Searcher.newWithBuffer(dbBinStr);
            //searchIpInfo 的数据格式： 国家|区域|省份|城市|ISP运营商
            String searchIpInfo = searcher.search(ip);
            // 库内获取不到的IP，访问ali的地域查询
            if (StrUtil.isNotEmpty(searchIpInfo)) {
                String[] splitIpInfo = searchIpInfo.split("\\|");
                // 获取所在国家
                result.setCountry(splitIpInfo[0]);
                // 获取所在省份
                result.setProvince(splitIpInfo[2]);
                // 获取所在城市
                result.setCity(splitIpInfo[3]);
            } else {
                result = getAlibaba(ip);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取 ip 所属地址
     *
     * @param ip IP地址
     * @return 所属地址
     */
    public static AdminDistrictInfo getAlibaba(String ip) {
        Map<String, Object> map = new HashMap<>();
        map.put("ip", ip);
        map.put("accessKey", "alibaba-inc");
        String result = HttpUtil.post("http://ip.taobao.com/outGetIpInfo", map);
        log.info("{} => POST: http://ip.taobao.com/outGetIpInfo || result: {}", ip, result);
        AdminDistrictInfo info = new AdminDistrictInfo();
        if (ObjectUtil.isNotEmpty(result)) {
            JSONObject jsonObject = JSONObject.parseObject(result);
            // 请求成功，解析响应数据
            if ("query success".equals(jsonObject.get("msg"))) {
                JSONObject dataMap = JSONObject.parseObject(jsonObject.getString("data"));
                info.setCountry(dataMap.getString("country"));
                info.setProvince(dataMap.getString("region"));
                info.setCity(dataMap.getString("city"));
            }
        }
        return info;
    }

    /**
     * 判断是否是内网IP
     *
     * @param ipAddress IP地址
     * @return
     */
    public static boolean internalIp(String ipAddress) {
        boolean isInnerIp;
        long ipNum = getIpNum(ipAddress);
        /*
         * 私有IP：A类 10.0.0.0-10.255.255.255 B类 172.16.0.0-172.31.255.255 C类
         * 192.168.0.0-192.168.255.255 当然，还有127这个网段是环回地址
         */
        long aBegin = getIpNum("10.0.0.0");
        long aEnd = getIpNum("10.255.255.255");
        long bBegin = getIpNum("172.16.0.0");
        long bEnd = getIpNum("172.31.255.255");
        long cBegin = getIpNum("192.168.0.0");
        long cEnd = getIpNum("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd)
                || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    /**
     * 获取IP数
     *
     * @param ipAddress IP地址
     * @return
     **/
    private static long getIpNum(String ipAddress) {
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }

    /**
     * 在 Nginx 等代理之后获取用户真实 IP 地址
     *
     * @return 用户的真实 IP 地址
     */
    public static String getIpAddress() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        // 从获取RequestAttributes中获取HttpServletRequest的信息
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        if (request == null) {
            return null;
        }
        String ip = request.getHeader("x-forwarded-for");
        if (isIpaddress(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isIpaddress(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isIpaddress(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (isIpaddress(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (isIpaddress(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                //根据网卡取本机配置的IP
                try {
                    InetAddress inet = InetAddress.getLocalHost();
                    ip = inet.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }
        return ip;
    }

    /**
     * 判断是否为 IP 地址
     *
     * @param ip IP 地址
     */
    public static boolean isIpaddress(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }

    public static void main(String[] args) {
        System.out.println(getRealAddress("112.10.247.9"));
    }

}
