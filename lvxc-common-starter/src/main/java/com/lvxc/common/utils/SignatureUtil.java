package com.lvxc.common.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * SDK签名工具类
 **/
public class SignatureUtil {
    private static final String SEPARATOR = "&";

    public static final Random random = new Random();

    /**
     * 创建SHA1签名
     *
     * @return SHA1签名
     */
    public static String createSignature(Map<String, Object> params) {
        return DigestUtils.sha1Hex(getSortedParamStr(params));
    }

    /**
     * 生成nonceStr，默认6位
     *
     * @return
     */
    public static String getNonceStr() {
        return getRandomStr(6);
    }

    /**
     * 生成随机字符串
     *
     * @return
     */
    public static String getRandomStr(int len) {
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 生成时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis() / 1000L;
    }

    /**
     * 根据参数名称对参数进行字典排序
     *
     * @param params
     * @return
     */
    private static String getSortedParamStr(Map<String, Object> params) {
        StringBuilder sb = new StringBuilder();
        Set<Map.Entry<String, Object>> entrySet = params.entrySet();
        Iterator<Map.Entry<String, Object>> it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry<String, Object> entry = it.next();
            String k = entry.getKey();
            Object v = entry.getValue();
            sb.append(k).append("=").append(v);
            if (it.hasNext()) sb.append(SEPARATOR);
        }
        return sb.toString();
    }
}
