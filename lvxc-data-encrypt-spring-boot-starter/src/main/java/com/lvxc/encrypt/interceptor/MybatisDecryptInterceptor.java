package com.lvxc.encrypt.interceptor;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lvxc.encrypt.annotation.EncryptAdvice;
import com.lvxc.encrypt.annotation.FieldEncrypt;
import com.lvxc.encrypt.properties.DataEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.sql.Statement;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

/**
 * mybatis拦截器，自动注入创建人、创建时间、修改人、修改时间
 *
 * @Author scott
 * @Date 2019-01-19
 */
@Slf4j
@Component
@ConditionalOnProperty(prefix = DataEncryptProperties.ENCRYPT_PREFIX, name = "enable", havingValue = "true", matchIfMissing = true)
@Intercepts({@Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class})})
public class MybatisDecryptInterceptor implements Interceptor {
    private final DataEncryptProperties encryptProperties;

    private final SymmetricCrypto defaultSymmetricCrypto;

    public MybatisDecryptInterceptor(DataEncryptProperties encryptProperties) {
        this.encryptProperties = encryptProperties;
        defaultSymmetricCrypto = new SymmetricCrypto(encryptProperties.getSymmetricAlgo(), HexUtil.decodeHex(encryptProperties.getSymmetricKey()));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object result = invocation.proceed();

        if (result instanceof Collection) {
            List resultList = (List) result;
            if (CollectionUtil.isNotEmpty(resultList)) {
                Class<?> resultType = resultList.get(0).getClass();
                EncryptAdvice encryptAdvice = resultType.getAnnotation(EncryptAdvice.class);
                if (encryptAdvice != null) {
                    Field[] fields = ReflectUtil.getFields(resultType);
                    for (Object dataObj : resultList) {
                        for (Field field : fields) {
                            try {
                                FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                                if (fieldEncrypt != null) {
                                    field.setAccessible(true);
                                    Object valueObj = field.get(dataObj);
                                    if (valueObj != null && valueObj instanceof String) {
                                        String value = (String) valueObj;
                                        if (StrUtil.isNotBlank(fieldEncrypt.symmetricKey())) {
                                            SymmetricCrypto symmetricCrypto = new SymmetricCrypto(encryptProperties.getSymmetricAlgo(), HexUtil.decodeHex(fieldEncrypt.symmetricKey()));
                                            value = symmetricCrypto.decryptStr(value);
                                        } else
                                            value = defaultSymmetricCrypto.decryptStr(value);
                                        field.set(dataObj, value);
                                    }
                                    field.setAccessible(false);
                                }
                            } catch (Exception e) {
                                log.error(e.getMessage());
                            }
                        }
                    }
                }
            }
        }

        return result;
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
