package com.lvxc.encrypt.interceptor;

import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import com.lvxc.encrypt.annotation.FieldEncrypt;
import com.lvxc.encrypt.properties.DataEncryptProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
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
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
public class MybatisEncryptInterceptor implements Interceptor {
    private final DataEncryptProperties encryptProperties;
    private final SymmetricCrypto defaultSymmetricCrypto;

    public MybatisEncryptInterceptor(DataEncryptProperties encryptProperties) {
        this.encryptProperties = encryptProperties;

        defaultSymmetricCrypto = new SymmetricCrypto(encryptProperties.getSymmetricAlgo(), HexUtil.decodeHex(encryptProperties.getSymmetricKey()));
    }

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];

        SqlCommandType sqlCommandType = mappedStatement.getSqlCommandType();
        Object parameter = invocation.getArgs()[1];

        if (parameter == null) {
            return invocation.proceed();
        }
        if (SqlCommandType.INSERT == sqlCommandType || SqlCommandType.UPDATE == sqlCommandType) {
            Field[] fields = ReflectUtil.getFields(parameter.getClass());
            for (Field field : fields) {
                try {
                    FieldEncrypt fieldEncrypt = field.getAnnotation(FieldEncrypt.class);
                    if (fieldEncrypt != null) {
                        field.setAccessible(true);
                        Object valueObj = field.get(parameter);
                        if (valueObj != null && valueObj instanceof String) {
                            String value = (String) valueObj;
                            if (StrUtil.isNotBlank(fieldEncrypt.symmetricKey())) {
                                SymmetricCrypto symmetricCrypto = new SymmetricCrypto(encryptProperties.getSymmetricAlgo(), HexUtil.decodeHex(fieldEncrypt.symmetricKey()));
                                value = symmetricCrypto.encryptHex(value);
                            } else
                                value = defaultSymmetricCrypto.encryptHex(value);
                            field.set(parameter, value);
                        }
                        field.setAccessible(false);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
    }
}
