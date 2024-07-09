package com.lvxc.enc.wrapper;

import com.lvxc.enc.EncryptablePropertyFilter;
import com.lvxc.enc.EncryptablePropertyResolver;
import com.lvxc.enc.caching.CachingDelegateEncryptablePropertySource;
import com.lvxc.enc.EncryptablePropertySource;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;

import java.util.Map;

/**
 * <p>EncryptableMapPropertySourceWrapper class.</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class EncryptableMapPropertySourceWrapper extends MapPropertySource implements EncryptablePropertySource<Map<String, Object>> {

    private final CachingDelegateEncryptablePropertySource<Map<String, Object>> encryptableDelegate;

    /**
     * <p>Constructor for EncryptableMapPropertySourceWrapper.</p>
     *
     * @param delegate a {@link MapPropertySource} object
     * @param resolver a {@link EncryptablePropertyResolver} object
     * @param filter a {@link EncryptablePropertyFilter} object
     */
    public EncryptableMapPropertySourceWrapper(MapPropertySource delegate, EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter) {
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
}
