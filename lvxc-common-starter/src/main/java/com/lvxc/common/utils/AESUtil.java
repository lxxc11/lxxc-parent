package com.lvxc.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Component
@Slf4j
public class AESUtil {

    public static final byte[] KEY = AESUtil.hexStringToBytes("d164e2fff3bc5bf19f8652b3d370ca4180ab92892efc6a484fbd5680b3f8890d");

    public static final String ALGORITHM = "AES";
    /**
     * 算法/模式/补码方式
     */
    private static final String ALGORITHM_PROVIDER = "AES/CTR/NoPadding";

    private static final String DEFAULT_IV = "AIHUOSHI520HSMAP";

    public static byte[] generatorKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        //默认128，获得无政策权限后可为192或256
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();
        return secretKey.getEncoded();
    }

    public static IvParameterSpec getIv() {
        //偏移量字符串必须是16位 当模式是CBC的时候必须设置偏移量
        IvParameterSpec ivParameterSpec = new IvParameterSpec(DEFAULT_IV.getBytes(StandardCharsets.UTF_8));
        return ivParameterSpec;
    }

    public static byte[] encrypt(String src, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(ALGORITHM_PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
        byte[] cipherBytes = cipher.doFinal(src.getBytes(StandardCharsets.UTF_8));
        return cipherBytes;
    }

    public static byte[] decrypt(String src, byte[] key) throws Exception {
        SecretKey secretKey = new SecretKeySpec(key, ALGORITHM);

        IvParameterSpec ivParameterSpec = getIv();
        Cipher cipher = Cipher.getInstance(ALGORITHM_PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameterSpec);
        byte[] hexBytes = hexStringToBytes(src);
        byte[] plainBytes = cipher.doFinal(hexBytes);
        return plainBytes;
    }

    /**
     * 将byte转换为16进制字符串
     * @param src
     * @return
     * 秘钥
     */
    public static String byteToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xff;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                sb.append("0");
            }
            sb.append(hv);
        }
        return sb.toString();
    }

    /**
     * 将16进制字符串装换为byte数组
     * @param hexString
     * @return
     */
    public static byte[] hexStringToBytes(String hexString) {
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] b = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            b[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return b;
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /**
     * 加密
     * src 需要加密的原文
     * key
     */
    public static String encryption(String src) throws NoSuchPaddingException, UnsupportedEncodingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        if(StringUtils.isNotEmpty(src)){
            return byteToHexString(encrypt(src,KEY));
        }
        return null;
    }

    /**
     *解密
     * src 需要解密的原文
     * key
     */
    public static String decryption (String src) throws Exception {
        if(StringUtils.isNotEmpty(src)){
            return new String(decrypt(src, KEY), StandardCharsets.UTF_8);
        }
        return null;
    }
    public static void main(String[] args) {
        try {
             //密钥必须是16的倍数
            String src = "ccccccccccc";
            System.out.println("原字符串:"+src);
            String enc = encryption(src);
            System.out.println("加密："+enc);
            System.out.println("解密："+decryption(enc));
        } catch (Exception e) {
            log.info("error");
        }
    }

}


