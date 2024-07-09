package com.lvxc.enc.wrapper;

import com.lvxc.enc.EncryptablePropertySourceConverter;
import com.lvxc.enc.configuration.EnvCopy;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.PropertySources;

/**
 * <p>EncryptableMutablePropertySourcesWrapper class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public class EncryptableMutablePropertySourcesWrapper extends MutablePropertySources {
    private final EncryptablePropertySourceConverter propertyConverter;
    private final EnvCopy envCopy;

    /**
     * <p>Constructor for EncryptableMutablePropertySourcesWrapper.</p>
     *
     * @param propertySources a {@link PropertySources} object
     * @param propertyConverter a {@link EncryptablePropertySourceConverter} object
     * @param envCopy a {@link EnvCopy} object
     */
    public EncryptableMutablePropertySourcesWrapper(PropertySources propertySources, EncryptablePropertySourceConverter propertyConverter, EnvCopy envCopy) {
        this(propertyConverter, envCopy);
        for (PropertySource<?> propertySource : propertySources) {
            super.addLast(propertySource);
        }
    }

    /**
     * <p>Constructor for EncryptableMutablePropertySourcesWrapper.</p>
     *
     * @param propertyConverter a {@link EncryptablePropertySourceConverter} object
     * @param envCopy a {@link EnvCopy} object
     */
    public EncryptableMutablePropertySourcesWrapper(EncryptablePropertySourceConverter propertyConverter, EnvCopy envCopy) {
        super();
        this.propertyConverter = propertyConverter;
        this.envCopy = envCopy;
    }

    private PropertySource<?> makeEncryptable(PropertySource<?> propertySource) {
        return propertyConverter.makeEncryptable(propertySource);
    }

    /** {@inheritDoc} */
    @Override
    public void addFirst(PropertySource<?> propertySource) {
        envCopy.addFirst(propertySource);
        super.addFirst(makeEncryptable(propertySource));
    }

    /** {@inheritDoc} */
    @Override
    public void addLast(PropertySource<?> propertySource) {
        envCopy.addLast(propertySource);
        super.addLast(makeEncryptable(propertySource));
    }

    /** {@inheritDoc} */
    @Override
    public void addBefore(String relativePropertySourceName, PropertySource<?> propertySource) {
        envCopy.addBefore(relativePropertySourceName, propertySource);
        super.addBefore(relativePropertySourceName, makeEncryptable(propertySource));
    }

    /** {@inheritDoc} */
    @Override
    public void addAfter(String relativePropertySourceName, PropertySource<?> propertySource) {
        envCopy.addAfter(relativePropertySourceName, propertySource);
        super.addAfter(relativePropertySourceName, makeEncryptable(propertySource));
    }

    /** {@inheritDoc} */
    @Override
    public void replace(String name, PropertySource<?> propertySource) {
        envCopy.replace(name, propertySource);
        super.replace(name, makeEncryptable(propertySource));
    }

    /** {@inheritDoc} */
    @Override
    public PropertySource<?> remove(String name) {
        envCopy.remove(name);
        return super.remove(name);
    }
}
