package com.lvxc.encrypt.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;

import javax.crypto.SecretKey;
import java.security.KeyPair;

/**
 * 非对称密钥对工具类
 *
 * @author 詹杨锋
 * @since 2023/12/1
 */
public class AsymmetricKeyUtil {
    public static String getSm4Key() {
        SecretKey secretKey = SmUtil.sm4().getSecretKey();
        return HexUtil.encodeHexStr(secretKey.getEncoded());
    }

    public static String[] generateAsymmetricKey(String algorithm) {
        KeyPair keyPair = SecureUtil.generateKeyPair(algorithm);

        String[] keyBase64Pair = new String[2];
        keyBase64Pair[0] = Base64.encode(keyPair.getPrivate().getEncoded());
        keyBase64Pair[1] = Base64.encode(keyPair.getPublic().getEncoded());
        return keyBase64Pair;
    }

    public static void main(String[] args) {
        String pubKey = "MFkwEwYHKoZIzj0CAQYIKoEcz1UBgi0DQgAEg/Iv/YyuQcokOoOvQSxmJb27YjkF0APGOCFPdel/r7AyJHQqpFfgXB3gyV/q6TxNrcnI061e+9udaYeb9sV2nQ==";
        String prvKey = "MIGTAgEAMBMGByqGSM49AgEGCCqBHM9VAYItBHkwdwIBAQQgJxQXG02TDuMf6ggPFbzM1TJ+i+BU1eCa82FxIkIxjW2gCgYIKoEcz1UBgi2hRANCAASD8i/9jK5ByiQ6g69BLGYlvbtiOQXQA8Y4IU916X+vsDIkdCqkV+BcHeDJX+rpPE2tycjTrV77251ph5v2xXad";

        AsymmetricCrypto asymmetricCrypto = new AsymmetricCrypto("SM2", prvKey, pubKey);
        String password = asymmetricCrypto.encryptHex("123456", KeyType.PublicKey);
        System.out.println(password);

        System.out.println(getSm4Key());
    }
}
