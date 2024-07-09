package com.lvxc.user.common.shiro.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lvxc.user.common.base.ResponseResult;
import com.lvxc.user.common.base.ResultEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.UnknownAccountException;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

/**
 * @author: Feng
 * @date: 2022年04月20日 18:18
 */
public class JwtUtil {

    /**
     * 有效时间，单位：秒
     */
    public static final long EXPIRE_TIME = 2 * 60 * 60;

    /**
     * @param response
     * @param resultEnum
     */
    public static void responseError(ServletResponse response, ResultEnum resultEnum) {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        httpServletResponse.setHeader("Content-type", "text/html;charset=UTF-8");
        ResponseResult jsonResult = new ResponseResult();
        jsonResult.setCode(resultEnum.getCode());
        jsonResult.setMessage(resultEnum.getMsg());
        OutputStream os = null;
        try {
            os = httpServletResponse.getOutputStream();
            httpServletResponse.setCharacterEncoding("UTF-8");
            httpServletResponse.setStatus(401);
            os.write(new ObjectMapper().writeValueAsString(jsonResult).getBytes("UTF-8"));
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 校验token是否正确
     *
     * @param token  密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String username, String secret) {
        try {
            // 根据密码生成JWT效验器
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).withClaim("username", username).build();
            // 效验TOKEN
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    /**
     * 获得token中的信息无需secret解密也能获得
     *
     * @return token中包含的用户名
     */
    public static String getUsername(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("username").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public static String getPlatformId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("platformId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * 生成签名,5min后过期
     *
     * @param username 用户名
     * @param secret   用户的密码
     * @return 加密的token
     */
    public static String sign(String username, String secret, String platformId) {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(secret);
        // 附带username信息
        //TODO: 这里可以在JWT信息里存入一个状态值pwdStatus，1可以访问，0只能访问重置密码相关接口
        return JWT.create()
                .withClaim("username", username)
                .withClaim("platformId", platformId)
                .withExpiresAt(date)
                .sign(algorithm);

    }

    /**
     * 根据request中的token获取用户账号
     *
     * @param request
     * @return
     * @throws UnknownAccountException
     */
    public static String getUserNameByToken(HttpServletRequest request) throws UnknownAccountException {
        String accessToken = request.getHeader("x-access-token");
        String username = getUsername(accessToken);
        if (StringUtils.isEmpty(username)) {
            throw new UnknownAccountException("未获取到用户");
        }
        return username;
    }


}