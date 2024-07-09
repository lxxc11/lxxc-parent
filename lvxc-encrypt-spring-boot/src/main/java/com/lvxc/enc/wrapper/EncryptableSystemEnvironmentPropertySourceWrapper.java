package com.lvxc.enc.wrapper;

import com.lvxc.enc.EncryptablePropertyFilter;
import com.lvxc.enc.EncryptablePropertyResolver;
import com.lvxc.enc.caching.CachingDelegateEncryptablePropertySource;
import com.lvxc.enc.EncryptablePropertySource;
import org.springframework.boot.origin.Origin;
import org.springframework.boot.origin.SystemEnvironmentOrigin;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.SystemEnvironmentPropertySource;

import java.util.Map;

/**
 * <p>EncryptableSystemEnvironmentPropertySourceWrapper class.</p>
 *
 * @author Tomas Tulka (@ttulka)
 * @version $Id: $Id
 */
public class EncryptableSystemEnvironmentPropertySourceWrapper extends SystemEnvironmentPropertySource implements EncryptablePropertySource<Map<String, Object>> {

    private final CachingDelegateEncryptablePropertySource<Map<String, Object>> encryptableDelegate;

    /**
     * <p>Constructor for EncryptableSystemEnvironmentPropertySourceWrapper.</p>
     *
     * @param delegate a {@link SystemEnvironmentPropertySource} object
     * @param resolver a {@link EncryptablePropertyResolver} object
     * @param filter a {@link EncryptablePropertyFilter} object
     */
    public EncryptableSystemEnvironmentPropertySourceWrapper(SystemEnvironmentPropertySource delegate, EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter) {
        super(delegate.getName(), delegate.getSource());
        encryptableDelegate = new CachingDelegateEncryptablePropertySource<>(delegate, resolver, filter);
    }

    /** {@inheritDoc} */
    @Override
    public Object getProperty(String name) {
        return encryptableDelegate.getProperty(name);
    }

    /** {@inheritDoc} */
    @Override
    public PropertySource<Map<String, Object>> getDelegate() {
        return encryptableDelegate;
    }

    /** {@inheritDoc} */
    @Override
    public Origin getOrigin(String key) {
        Origin fromSuper = EncryptablePropertySource.super.getOrigin(key);
        if (fromSuper != null) {
            return fromSuper;
        }
        String property = resolvePropertyName(key);
        if (super.containsProperty(property)) {
            return new SystemEnvironmentOrigin(property);
        }
        return null;
    }
}
