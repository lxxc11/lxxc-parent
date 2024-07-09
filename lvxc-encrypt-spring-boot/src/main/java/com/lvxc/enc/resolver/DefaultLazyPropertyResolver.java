package com.lvxc.enc.resolver;

import com.lvxc.enc.EncryptablePropertyDetector;
import com.lvxc.enc.EncryptablePropertyResolver;
import com.lvxc.enc.util.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.Environment;

import java.util.Optional;

import static com.lvxc.enc.util.Functional.tap;

/**
 * Default Resolver bean that delegates to a custom defined {@link EncryptablePropertyResolver} or creates a new {@link DefaultPropertyResolver}
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
@Slf4j
public class DefaultLazyPropertyResolver implements EncryptablePropertyResolver {

    private Singleton<EncryptablePropertyResolver> singleton;

    /**
     * <p>Constructor for DefaultLazyPropertyResolver.</p>
     *
     * @param propertyDetector a {@link EncryptablePropertyDetector} object
     * @param encryptor a {@link StringEncryptor} object
     * @param customResolverBeanName a {@link String} object
     * @param isCustom a boolean
     * @param bf a {@link BeanFactory} object
     * @param environment a {@link Environment} object
     */
    public DefaultLazyPropertyResolver(EncryptablePropertyDetector propertyDetector, StringEncryptor encryptor, String customResolverBeanName, boolean isCustom, BeanFactory bf, Environment environment) {
        singleton = new Singleton<>(() ->
                Optional.of(customResolverBeanName)
                        .filter(bf::containsBean)
                        .map(name -> (EncryptablePropertyResolver) bf.getBean(name))
                        .map(tap(bean -> log.info("Found Custom Resolver Bean {} with name: {}", bean, customResolverBeanName)))
                        .orElseGet(() -> {
                            if (isCustom) {
                                throw new IllegalStateException(String.format("Property Resolver custom Bean not found with name '%s'", customResolverBeanName));
                            }
                            log.info("Property Resolver custom Bean not found with name '{}'. Initializing Default Property Resolver", customResolverBeanName);
                            return createDefault(propertyDetector, encryptor, environment);
                        }));
    }

    /**
     * <p>Constructor for DefaultLazyPropertyResolver.</p>
     *
     * @param propertyDetector a {@link EncryptablePropertyDetector} object
     * @param encryptor a {@link StringEncryptor} object
     * @param environment a {@link Environment} object
     */
    public DefaultLazyPropertyResolver(EncryptablePropertyDetector propertyDetector, StringEncryptor encryptor, Environment environment) {
        singleton = new Singleton<>(() -> createDefault(propertyDetector, encryptor, environment));
    }

    private DefaultPropertyResolver createDefault(EncryptablePropertyDetector propertyDetector, StringEncryptor encryptor, Environment environment) {
        return new DefaultPropertyResolver(encryptor, propertyDetector, environment);
    }

    /** {@inheritDoc} */
    @Override
    public String resolvePropertyValue(String value) {
        return singleton.get().resolvePropertyValue(value);
    }
}
