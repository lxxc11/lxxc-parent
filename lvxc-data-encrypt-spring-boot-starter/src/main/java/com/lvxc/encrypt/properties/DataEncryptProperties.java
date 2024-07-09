package com.lvxc.encrypt.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = DataEncryptProperties.ENCRYPT_PREFIX)
@Data
public class DataEncryptProperties {
    public static final String ENCRYPT_PREFIX = "lvxc.data.encrypt";

    private boolean enable = false;

    private String asymmetricAlgo = "SM2";

    private String pubKey;

    private String prvKey;

    private String digestAlgo = "SM3";

    private int digestTimes = -1;

    private String digestSalt = "";

    private int digestSaltPosition = -1;

    private String symmetricAlgo = "SM4";

    private String symmetricKey = "lvxc";


}
