package com.lvxc.enc;

import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.OriginLookup;
import org.springframework.core.env.PropertySource;

/**
 * <p>EncryptablePropertySource interface.</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public interface EncryptablePropertySource<T> extends OriginLookup<String> {

    /**
     * <p>getDelegate.</p>
     *
     * @return a {@link PropertySource} object
     */
    PropertySource<T> getDelegate();

    /**
     * <p>getProperty.</p>
     *
     * @param name a {@link String} object
     * @return a {@link Object} object
     */
    default Object getProperty(String name) {
        return getDelegate().getProperty(name);
    };

    /**
     * <p>refresh.</p>
     */
    default void refresh() {
        if(getDelegate() instanceof EncryptablePropertySource) {
            ((EncryptablePropertySource<?>) getDelegate()).refresh();
        }
    }

    /**
     * <p>getProperty.</p>
     *
     * @param resolver a {@link EncryptablePropertyResolver} object
     * @param filter a {@link EncryptablePropertyFilter} object
     * @param source a {@link PropertySource} object
     * @param name a {@link String} object
     * @return a {@link Object} object
     */
    default Object getProperty(EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter, PropertySource<T> source, String name) {
        Object value = source.getProperty(name);
        if (value != null && filter.shouldInclude(source, name) && value instanceof String) {
            String stringValue = String.valueOf(value);
            return resolver.resolvePropertyValue(stringValue);
        }
        return value;
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    @Override
    default Origin getOrigin(String key) {
        if(getDelegate() instanceof OriginLookup) {
            return ((OriginLookup<String>) getDelegate()).getOrigin(key);
        }
        return null;
    }

    /** {@inheritDoc} */
    @Override
    default boolean isImmutable() {
        if(getDelegate() instanceof OriginLookup) {
            return ((OriginLookup<?>) getDelegate()).isImmutable();
        }
        return OriginLookup.super.isImmutable();
    }

    /** {@inheritDoc} */
    @Override
    default String getPrefix() {
        if(getDelegate() instanceof OriginLookup) {
            return ((OriginLookup<?>) getDelegate()).getPrefix();
        }
        return OriginLookup.super.getPrefix();
    }
}
