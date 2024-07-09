package com.lvxc.enc.environment;

import com.lvxc.enc.*;
import com.lvxc.enc.configuration.EnvCopy;
import com.lvxc.enc.detector.DefaultLazyPropertyDetector;
import com.lvxc.enc.encryptor.DefaultLazyEncryptor;
import com.lvxc.enc.filter.DefaultLazyPropertyFilter;
import com.lvxc.enc.resolver.DefaultLazyPropertyResolver;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * <p>EnvironmentInitializer class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
@Slf4j
public class EnvironmentInitializer {
    private final InterceptionMode interceptionMode;
    private final List<Class<PropertySource<?>>> skipPropertySourceClasses;
    private final EncryptablePropertyResolver resolver;
    private final EncryptablePropertyFilter filter;
    private final StringEncryptor encryptor;
    private final EncryptablePropertyDetector detector;
    private final InterceptionMode propertySourceInterceptionMode;

    /**
     * <p>Constructor for EnvironmentInitializer.</p>
     *
     * @param interceptionMode a {@link InterceptionMode} object
     * @param propertySourceInterceptionMode a {@link InterceptionMode} object
     * @param skipPropertySourceClasses a {@link List} object
     * @param resolver a {@link EncryptablePropertyResolver} object
     * @param filter a {@link EncryptablePropertyFilter} object
     * @param encryptor a {@link StringEncryptor} object
     * @param detector a {@link EncryptablePropertyDetector} object
     */
    public EnvironmentInitializer(InterceptionMode interceptionMode, InterceptionMode propertySourceInterceptionMode, List<Class<PropertySource<?>>> skipPropertySourceClasses, EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter, StringEncryptor encryptor, EncryptablePropertyDetector detector) {

        this.interceptionMode = interceptionMode;
        this.propertySourceInterceptionMode = propertySourceInterceptionMode;
        this.skipPropertySourceClasses = skipPropertySourceClasses;
        this.resolver = resolver;
        this.filter = filter;
        this.encryptor = encryptor;
        this.detector = detector;
    }

    void initialize(EncryptableEnvironment environment) {
        log.info("Initializing Environment: {}", environment.getClass().getSimpleName());
        InterceptionMode actualInterceptionMode = Optional.ofNullable(interceptionMode).orElse(InterceptionMode.WRAPPER);
        List<Class<PropertySource<?>>> actualSkipPropertySourceClasses = Optional.ofNullable(skipPropertySourceClasses).orElseGet(Collections::emptyList);
        EnvCopy envCopy = new EnvCopy(environment);
        EncryptablePropertyFilter actualFilter = Optional.ofNullable(filter).orElseGet(() -> new DefaultLazyPropertyFilter(envCopy.get()));
        StringEncryptor actualEncryptor = Optional.ofNullable(encryptor).orElseGet(() -> new DefaultLazyEncryptor(envCopy.get()));
        EncryptablePropertyDetector actualDetector = Optional.ofNullable(detector).orElseGet(() -> new DefaultLazyPropertyDetector(envCopy.get()));
        EncryptablePropertyResolver actualResolver = Optional.ofNullable(resolver).orElseGet(() -> new DefaultLazyPropertyResolver(actualDetector, actualEncryptor, environment));
        EncryptablePropertySourceConverter converter = new EncryptablePropertySourceConverter(actualInterceptionMode, actualSkipPropertySourceClasses, actualResolver, actualFilter);
        converter.convertPropertySources(environment.getOriginalPropertySources());
        MutablePropertySources encryptableSources = converter.convertMutablePropertySources(propertySourceInterceptionMode, environment.getOriginalPropertySources(), envCopy);
        environment.setEncryptablePropertySources(encryptableSources);
    }

    static MutableConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
        return new MutableConfigurablePropertyResolver(propertySources, ConfigurationPropertySources::createPropertyResolver);
    }

}
