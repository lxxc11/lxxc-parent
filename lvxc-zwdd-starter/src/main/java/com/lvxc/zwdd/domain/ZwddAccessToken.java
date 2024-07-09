package com.lvxc.zwdd.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ZwddAccessToken implements Serializable {
    /**
     * 过期时间，单位 秒
     */
    private String expiresIn;
    /**
     * accessToken
     */
    private String accessToken;
}
