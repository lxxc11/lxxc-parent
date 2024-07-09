package com.lvxc.encrypt.handler;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.digest.Digester;
import com.lvxc.encrypt.annotation.EncryptAdvice;
import com.lvxc.encrypt.annotation.ParamEncrypt;
import com.lvxc.encrypt.exception.CryptoConfigNotFoundException;
import com.lvxc.encrypt.properties.DataEncryptProperties;
import lombok.SneakyThrows;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

@RestControllerAdvice
@ConditionalOnProperty(prefix = DataEncryptProperties.ENCRYPT_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
public class ParamEncryptRequestBodyAdvice implements RequestBodyAdvice {
    private final DataEncryptProperties encryptProperties;
    private final AsymmetricCrypto asymmetricCrypto;

    private final Digester defaultDigester;

    public ParamEncryptRequestBodyAdvice(DataEncryptProperties encryptProperties) {
        this.encryptProperties = encryptProperties;
        if (StrUtil.isNotBlank(encryptProperties.getAsymmetricAlgo()) && StrUtil.isNotBlank(encryptProperties.getPrvKey()) && StrUtil.isNotBlank(encryptProperties.getPubKey()))
            asymmetricCrypto = new AsymmetricCrypto(encryptProperties.getAsymmetricAlgo(), Base64.decode(encryptProperties.getPrvKey()), Base64.decode(encryptProperties.getPubKey()));
        else
            asymmetricCrypto = null;

        if (StrUtil.isNotBlank(encryptProperties.getDigestAlgo())) {
            defaultDigester = new Digester(encryptProperties.getDigestAlgo());
            if (StrUtil.isNotBlank(encryptProperties.getDigestSalt())) {
                defaultDigester.setSalt(encryptProperties.getDigestSalt().getBytes());
                if (encryptProperties.getDigestSaltPosition() > -1)
                    defaultDigester.setSaltPosition(encryptProperties.getDigestSaltPosition());
            }
            defaultDigester.setDigestCount(encryptProperties.getDigestTimes());
        } else
            defaultDigester = null;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return methodParameter.hasMethodAnnotation(EncryptAdvice.class);
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @SneakyThrows
    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Class<?> clazz = body.getClass();
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                ParamEncrypt paramEncrypt = field.getAnnotation(ParamEncrypt.class);
                if (paramEncrypt != null) {
                    field.setAccessible(true);
                    Object valueObj = field.get(body);
                    if (valueObj != null && valueObj instanceof String) {
                        String value = (String) valueObj;
                        if (StrUtil.isNotBlank(value)) {
                            if (paramEncrypt.needDecrypt())
                                if (asymmetricCrypto == null)
                                    throw new CryptoConfigNotFoundException("请补充非对称加密配置");
                            value = asymmetricCrypto.decryptStr(value, KeyType.PrivateKey);

                            if (paramEncrypt.digest()) {
                                if (defaultDigester == null)
                                    throw new CryptoConfigNotFoundException("请补充摘要加密配置");
                                if (StrUtil.isNotBlank(paramEncrypt.digestSalt()) || paramEncrypt.digestTimes() > -1 || paramEncrypt.digestSaltPosition() > -1) {
                                    Digester digester = new Digester(encryptProperties.getDigestAlgo());
                                    digester.setSalt(StrUtil.isNotBlank(paramEncrypt.digestSalt()) ? paramEncrypt.digestSalt().getBytes() : encryptProperties.getDigestSalt().getBytes());
                                    digester.setDigestCount(paramEncrypt.digestTimes() > -1 ? paramEncrypt.digestTimes() : encryptProperties.getDigestTimes());
                                    digester.setSaltPosition(paramEncrypt.digestSaltPosition() > -1 ? paramEncrypt.digestSaltPosition() : encryptProperties.getDigestSaltPosition());
                                    value = digester.digestHex(value);
                                } else {
                                    value = defaultDigester.digestHex(value);
                                }
                            }
                            field.set(body, value);
                        }
                    }
                    field.setAccessible(false);
                }
            }
            clazz = clazz.getSuperclass();
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
