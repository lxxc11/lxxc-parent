package com.lvxc.enc.filter;

import com.lvxc.enc.EncryptablePropertyFilter;
import com.lvxc.enc.properties.JasyptEncryptorConfigurationProperties;
import com.lvxc.enc.util.Functional;
import com.lvxc.enc.util.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;

import java.util.Optional;

/**
 * <p>DefaultLazyPropertyFilter class.</p>
 *
 * @author Sergio.U.Bocchio
 * @version $Id: $Id
 */
@Slf4j
public class DefaultLazyPropertyFilter implements EncryptablePropertyFilter {

    private Singleton<EncryptablePropertyFilter> singleton;

    /**
     * <p>Constructor for DefaultLazyPropertyFilter.</p>
     *
     * @param e a {@link ConfigurableEnvironment} object
     * @param customFilterBeanName a {@link String} object
     * @param isCustom a boolean
     * @param bf a {@link BeanFactory} object
     */
    public DefaultLazyPropertyFilter(ConfigurableEnvironment e, String customFilterBeanName, boolean isCustom, BeanFactory bf) {
        singleton = new Singleton<>(() ->
                Optional.of(customFilterBeanName)
                        .filter(bf::containsBean)
                        .map(name -> (EncryptablePropertyFilter) bf.getBean(name))
                        .map(Functional.tap(bean -> log.info("Found Custom Filter Bean {} with name: {}", bean, customFilterBeanName)))
                        .orElseGet(() -> {
                            if (isCustom) {
                                throw new IllegalStateException(String.format("Property Filter custom Bean not found with name '%s'", customFilterBeanName));
                            }

                            log.info("Property Filter custom Bean not found with name '{}'. Initializing Default Property Filter", customFilterBeanName);
                            return createDefault(e);
                        }));
    }

    /**
     * <p>Constructor for DefaultLazyPropertyFilter.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object
     */
    public DefaultLazyPropertyFilter(ConfigurableEnvironment environment) {
        singleton = new Singleton<>(() -> createDefault(environment));
    }

    private DefaultPropertyFilter createDefault(ConfigurableEnvironment environment) {
        JasyptEncryptorConfigurationProperties props = JasyptEncryptorConfigurationProperties.bindConfigProps(environment);
        final JasyptEncryptorConfigurationProperties.PropertyConfigurationProperties.FilterConfigurationProperties filterConfig = props.getProperty().getFilter();
        return new DefaultPropertyFilter(filterConfig.getIncludeSources(), filterConfig.getExcludeSources(),
                filterConfig.getIncludeNames(), filterConfig.getExcludeNames());
    }

    /** {@inheritDoc} */
    @Override
    public boolean shouldInclude(PropertySource<?> source, String name) {
        return singleton.get().shouldInclude(source, name);
    }
}
