package com.lvxc.enc.environment;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MutablePropertySources;

/**
 * <p>EncryptableEnvironment interface.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
public interface EncryptableEnvironment extends ConfigurableEnvironment {
    /**
     * <p>getOriginalPropertySources.</p>
     *
     * @return a {@link MutablePropertySources} object
     */
    MutablePropertySources getOriginalPropertySources();
    /**
     * <p>setEncryptablePropertySources.</p>
     *
     * @param propertySources a {@link MutablePropertySources} object
     */
    void setEncryptablePropertySources(MutablePropertySources propertySources);
}
