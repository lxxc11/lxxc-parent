package com.lvxc.enc.configuration;

import com.lvxc.enc.EncryptablePropertySource;
import com.lvxc.enc.environment.EncryptableEnvironment;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

import java.util.Optional;

/**
 * Need a copy of the environment without the Enhanced property sources to avoid circular dependencies.
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public class EnvCopy {
    StandardEnvironment copy;

    /**
     * <p>Constructor for EnvCopy.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object
     */
    public EnvCopy(final ConfigurableEnvironment environment) {
        copy = new StandardEnvironment();
        Optional
                .ofNullable(environment instanceof EncryptableEnvironment ? ((EncryptableEnvironment) environment).getOriginalPropertySources() : environment.getPropertySources())
                .ifPresent(sources -> sources.forEach(this::addLast));
    }

    @SuppressWarnings({"rawtypes"})
    private PropertySource<?> getOriginal(PropertySource<?> propertySource) {
        return propertySource instanceof EncryptablePropertySource
                ? ((EncryptablePropertySource) propertySource).getDelegate()
                : propertySource;
    }

    /**
     * <p>isAllowed.</p>
     *
     * @param propertySource a {@link PropertySource} object
     * @return a boolean
     */
    public boolean isAllowed(PropertySource<?> propertySource) {
        final PropertySource<?> original = getOriginal(propertySource);
        return !original.getClass().getName().equals("org.springframework.boot.context.properties.source.ConfigurationPropertySourcesPropertySource");
    }

    /**
     * <p>addFirst.</p>
     *
     * @param propertySource a {@link PropertySource} object
     */
    public void addFirst(PropertySource<?> propertySource) {
        if (isAllowed(propertySource)) {
            final PropertySource<?> original = getOriginal(propertySource);
            copy.getPropertySources().addFirst(original);
        }
    }

    /**
     * <p>addLast.</p>
     *
     * @param propertySource a {@link PropertySource} object
     */
    public void addLast(PropertySource<?> propertySource) {
        if (isAllowed(propertySource)) {
            final PropertySource<?> original = getOriginal(propertySource);
            copy.getPropertySources().addLast(original);
        }
    }

    /**
     * <p>addBefore.</p>
     *
     * @param relativePropertySourceName a {@link String} object
     * @param propertySource a {@link PropertySource} object
     */
    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (isAllowed(propertySource)) {
            final PropertySource<?> original = getOriginal(propertySource);
            copy.getPropertySources().addBefore(relativePropertySourceName, original);
        }
    }

    /**
     * <p>addAfter.</p>
     *
     * @param relativePropertySourceName a {@link String} object
     * @param propertySource a {@link PropertySource} object
     */
    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
        if (isAllowed(propertySource)) {
            final PropertySource<?> original = getOriginal(propertySource);
            copy.getPropertySources().addAfter(relativePropertySourceName, original);
        }
    }

    /**
     * <p>replace.</p>
     *
     * @param name a {@link String} object
     * @param propertySource a {@link PropertySource} object
     */
    public void replace(String name, PropertySource<?> propertySource) {
        if(isAllowed(propertySource)) {
            if(copy.getPropertySources().contains(name)) {
                final PropertySource<?> original = getOriginal(propertySource);
                copy.getPropertySources().replace(name, original);
            }
        }
    }

    /**
     * <p>remove.</p>
     *
     * @param name a {@link String} object
     * @return a {@link PropertySource} object
     */
    public PropertySource<?> remove(String name) {
        return copy.getPropertySources().remove(name);
    }

    /**
     * <p>get.</p>
     *
     * @return a {@link ConfigurableEnvironment} object
     */
    public ConfigurableEnvironment get() {
        return copy;
    }
}
