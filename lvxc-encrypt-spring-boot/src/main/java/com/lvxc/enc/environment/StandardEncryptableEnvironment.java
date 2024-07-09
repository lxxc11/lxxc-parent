package com.lvxc.enc.environment;

import com.lvxc.enc.EncryptablePropertyDetector;
import com.lvxc.enc.EncryptablePropertyFilter;
import com.lvxc.enc.EncryptablePropertyResolver;
import com.lvxc.enc.InterceptionMode;
import com.lvxc.enc.detector.DefaultLazyPropertyDetector;
import com.lvxc.enc.encryptor.DefaultLazyEncryptor;
import com.lvxc.enc.filter.DefaultLazyPropertyFilter;
import com.lvxc.enc.resolver.DefaultLazyPropertyResolver;
import lombok.Builder;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.env.*;

import java.util.List;

/**
 * A custom {@link ConfigurableEnvironment} that is useful for
 * early access of encrypted properties on bootstrap. While not required in most scenarios
 * could be useful when customizing Spring Boot's init behavior or integrating with certain capabilities that are
 * configured very early, such as Logging configuration. For a concrete example, this method of enabling encryptable
 * properties is the only one that works with Spring Properties replacement in logback-spring.xml files, using the
 * springProperty tag
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public class StandardEncryptableEnvironment extends StandardEnvironment implements ConfigurableEnvironment, EncryptableEnvironment {

    private MutablePropertySources encryptablePropertySources;
    private ConfigurablePropertyResolver pr;

    /**
     * <p>Constructor for StandardEncryptableEnvironment.</p>
     */
    public StandardEncryptableEnvironment() {
        this(null, null, null, null, null, null, null);
    }

    /**
     * Create a new Encryptable Environment. All arguments are optional, provide null if default value is desired.
     *
     * @param interceptionMode          The interception method to utilize, or null (Default is {@link InterceptionMode#WRAPPER})
     * @param propertySourcesInterceptionMode          The interception method to utilize for wrapping the {@link MutablePropertySources}, or null (Default is {@link InterceptionMode#WRAPPER})
     * @param skipPropertySourceClasses A list of {@link PropertySource} classes to skip from interception, or null (Default is empty)
     * @param resolver                  The property resolver to utilize, or null (Default is {@link DefaultLazyPropertyResolver}  which will resolve to specified configuration)
     * @param filter                    The property filter to utilize, or null (Default is {@link DefaultLazyPropertyFilter}  which will resolve to specified configuration)
     * @param encryptor                 The string encryptor to utilize, or null (Default is {@link DefaultLazyEncryptor} which will resolve to specified configuration)
     * @param detector                  The property detector to utilize, or null (Default is {@link DefaultLazyPropertyDetector} which will resolve to specified configuration)
     */
    @Builder
    public StandardEncryptableEnvironment(InterceptionMode interceptionMode, InterceptionMode propertySourcesInterceptionMode, List<Class<PropertySource<?>>> skipPropertySourceClasses, EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter, StringEncryptor encryptor, EncryptablePropertyDetector detector) {
        EnvironmentInitializer initializer = new EnvironmentInitializer(interceptionMode, propertySourcesInterceptionMode, skipPropertySourceClasses, resolver, filter, encryptor, detector);
        initializer.initialize(this);
    }

    /** {@inheritDoc} */
    @Override
    protected void customizePropertySources(MutablePropertySources propertySources) {
        super.customizePropertySources(propertySources);
    }

    /** {@inheritDoc} */
    @Override
    public MutablePropertySources getPropertySources() {
        return this.encryptablePropertySources;
    }

    /** {@inheritDoc} */
    @Override
    public MutablePropertySources getOriginalPropertySources() {
        return super.getPropertySources();
    }

    /** {@inheritDoc} */
    @Override
    public void setEncryptablePropertySources(MutablePropertySources propertySources) {
        this.encryptablePropertySources = propertySources;
        ((MutableConfigurablePropertyResolver)this.getPropertyResolver()).setPropertySources(propertySources);
    }

    /** {@inheritDoc} */
    @Override
    protected ConfigurablePropertyResolver createPropertyResolver(MutablePropertySources propertySources) {
        return EnvironmentInitializer.createPropertyResolver(propertySources);
    }
}
