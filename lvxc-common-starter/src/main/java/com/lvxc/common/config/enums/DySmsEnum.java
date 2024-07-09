package com.lvxc.common.config.enums;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
public enum DySmsEnum {

    /**
     * 短信模板
     */
    SMS_CODE ("SMS_139972794", "", "您的验证码是{code},15分钟内有效,如非本人操作,请忽略本短信。");


    /**
     * 短信模板编码
     */
    private final String templateCode;
    /**
     * 签名
     */
    private final String signName;

    private final String content;

    DySmsEnum(String templateCode, String signName, String content) {
        this.templateCode = templateCode;
        this.signName = signName;
        this.content = content;
    }

    public static DySmsEnum toEnum(String templateCode) {
        if (StringUtils.isEmpty (templateCode)) {
            return null;
        }
        for (DySmsEnum item : DySmsEnum.values ()) {
            if (item.getTemplateCode ().equals (templateCode)) {
                return item;
            }
        }
        return null;
    }
}

