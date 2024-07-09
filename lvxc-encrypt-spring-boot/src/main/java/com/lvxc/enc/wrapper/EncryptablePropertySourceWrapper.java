package com.lvxc.enc.wrapper;

import com.lvxc.enc.EncryptablePropertyFilter;
import com.lvxc.enc.EncryptablePropertyResolver;
import com.lvxc.enc.caching.CachingDelegateEncryptablePropertySource;
import com.lvxc.enc.EncryptablePropertySource;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.core.env.PropertySource;

/**
 * <p>Wrapper for {@link PropertySource} instances that simply delegates the {@link #getProperty} method
 * to the {@link PropertySource} delegate instance to retrieve properties, while checking if the resulting
 * property is encrypted or not using the Jasypt convention of surrounding encrypted values with "ENC()".</p>
 * <p>When an encrypted property is detected, it is decrypted using the provided {@link StringEncryptor}</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class EncryptablePropertySourceWrapper<T> extends PropertySource<T> implements EncryptablePropertySource<T> {
    private final CachingDelegateEncryptablePropertySource<T> encryptableDelegate;

    /**
     * <p>Constructor for EncryptablePropertySourceWrapper.</p>
     *
     * @param delegate a {@link PropertySource} object
     * @param resolver a {@link EncryptablePropertyResolver} object
     * @param filter a {@link EncryptablePropertyFilter} object
     */
    public EncryptablePropertySourceWrapper(PropertySource<T> delegate, EncryptablePropertyResolver resolver, EncryptablePropertyFilter filter) {
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
    public PropertySource<T> getDelegate() {
        return encryptableDelegate;
    }
}
