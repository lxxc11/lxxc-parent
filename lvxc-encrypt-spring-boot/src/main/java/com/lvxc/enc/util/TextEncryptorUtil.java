package com.lvxc.enc.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

public class TextEncryptorUtil  {

    private final static StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();


    public static String encrypt(String message) {
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setPassword("lvxc");
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor.encrypt(message);
    }

    public static String encrypt(String message,String algorithm) {
        encryptor.setAlgorithm(algorithm);
        encryptor.setPassword("lvxc");
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor.encrypt(message);
    }

    public static String encrypt(String message,String password,String algorithm) {
        encryptor.setAlgorithm(algorithm);
        encryptor.setPassword(password);
        encryptor.setIvGenerator(new RandomIvGenerator());
        return encryptor.encrypt(message);
    }

}
