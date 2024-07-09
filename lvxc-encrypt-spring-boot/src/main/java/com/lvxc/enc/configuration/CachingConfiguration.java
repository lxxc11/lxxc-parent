package com.lvxc.enc.configuration;

import com.lvxc.enc.EncryptablePropertySourceConverter;
import com.lvxc.enc.caching.RefreshScopeRefreshedEventListener;
import com.lvxc.enc.properties.JasyptEncryptorConfigurationProperties;
import com.lvxc.enc.util.Singleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * <p>CachingConfiguration class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
@Configuration
public class CachingConfiguration {
    /**
     * <p>refreshScopeRefreshedEventListener.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object
     * @param converter a {@link EncryptablePropertySourceConverter} object
     * @param config a {@link Singleton} object
     * @return a {@link RefreshScopeRefreshedEventListener} object
     */
    @Bean
    public RefreshScopeRefreshedEventListener refreshScopeRefreshedEventListener(ConfigurableEnvironment environment, EncryptablePropertySourceConverter converter, Singleton<JasyptEncryptorConfigurationProperties> config) {
        return new RefreshScopeRefreshedEventListener(environment, converter, config.get());
    }
}
