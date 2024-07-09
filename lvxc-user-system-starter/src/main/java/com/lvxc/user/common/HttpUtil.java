package com.lvxc.user.common;

import cn.hutool.core.map.MapUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class HttpUtil {

    public static <T> T postRequestByUrlencoded(String url, MultiValueMap<String, String> params, Map<String, String> basicAuthMap, Class<T> clazz, int againNum) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        if (!MapUtil.isEmpty(basicAuthMap)) {
            headers.setBasicAuth(basicAuthMap.get("username"), basicAuthMap.get("password"));
        }
        HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<>(params, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            log.info(url + "请求的结果为：" + response);
            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                if (body != null) {
                    Gson gson = new GsonBuilder().create();
                    return gson.fromJson(body, clazz);
                }
                return null;
            }
        } catch (Exception e) {
            Thread.sleep(1000);
            againNum--;
            if (againNum <= 0) {
                log.error(e.getMessage());
                throw new AuthenticationException("远程调用第三方接口失败，url：" + url + "，参数：" + httpEntity);
            }
            postRequestByUrlencoded(url, params, basicAuthMap, clazz, againNum);
        }
        return null;
    }


    public static <T> T getRequestByUrlencoded(String url, HashMap<String, Object> params, Class<T> clazz, int againNum) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        //params 请求参数拼接
        String getUrl = "";
        if (params != null && params.size() > 0) {
            StringBuilder urlbuilder = new StringBuilder(url);
            urlbuilder.append("?");
            for (Map.Entry<String, Object> param : params.entrySet()) {
                urlbuilder.append(param.getKey()).append("=").append(param.getValue()).append("&");
            }
            getUrl = urlbuilder.substring(0, urlbuilder.length() - 1);
        }
        try {
            String result = restTemplate.getForObject(getUrl, String.class);
            if (result != null) {
                Gson gson = new GsonBuilder().create();
                return gson.fromJson(result, clazz);
            }
        } catch (Exception e) {
            Thread.sleep(1000);
            againNum--;
            if (againNum <= 0) {
                log.error(e.getMessage());
                throw new AuthenticationException("远程调用第三方接口失败");
            }
            getRequestByUrlencoded(url, params, clazz, againNum);
        }
        return null;
    }


    /*****************************json*******************************/

    public static <T> T postRequestByJson(String url, String jsonObject, String bearerAuth, Class<T> clazz, int againNum) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_PLAIN);
        if (!StringUtils.isEmpty(bearerAuth)) {
            headers.setBearerAuth(bearerAuth);
        }
        HttpEntity<Object> httpEntity = new HttpEntity<>(jsonObject, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            log.info(url + "请求的结果为：" + response);
            if (response != null && response.getStatusCode() == HttpStatus.OK) {
                String body = response.getBody();
                if (body != null) {
                    Gson gson = new GsonBuilder().create();
                    return gson.fromJson(body, clazz);
                }
                return null;
            }
        } catch (Exception e) {
            Thread.sleep(1000);
            againNum--;
            if (againNum <= 0) {
                log.error(e.getMessage());
                throw new AuthenticationException("远程调用第三方接口失败，url：" + url + "，参数：" + httpEntity);
            }
            postRequestByJson(url, jsonObject, bearerAuth, clazz, againNum);
        }
        return null;
    }

}
