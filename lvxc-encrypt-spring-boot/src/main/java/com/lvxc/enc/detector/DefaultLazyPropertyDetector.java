package com.lvxc.enc.detector;

import com.lvxc.enc.EncryptablePropertyDetector;
import com.lvxc.enc.properties.JasyptEncryptorConfigurationProperties;
import com.lvxc.enc.util.Singleton;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Optional;

import static com.lvxc.enc.util.Functional.tap;

/**
 * Default Lazy property detector that delegates to a custom {@link EncryptablePropertyDetector} bean or initializes a
 * default {@link DefaultPropertyDetector}.
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
@Slf4j
public class DefaultLazyPropertyDetector implements EncryptablePropertyDetector {

    private Singleton<EncryptablePropertyDetector> singleton;

    /**
     * <p>Constructor for DefaultLazyPropertyDetector.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object
     * @param customDetectorBeanName a {@link String} object
     * @param isCustom a boolean
     * @param bf a {@link BeanFactory} object
     */
    public DefaultLazyPropertyDetector(ConfigurableEnvironment environment, String customDetectorBeanName, boolean isCustom, BeanFactory bf) {
        singleton = new Singleton<>(() ->
                Optional.of(customDetectorBeanName)
                        .filter(bf::containsBean)
                        .map(name -> (EncryptablePropertyDetector) bf.getBean(name))
                        .map(tap(bean -> log.info("Found Custom Detector Bean {} with name: {}", bean, customDetectorBeanName)))
                        .orElseGet(() -> {
                            if(isCustom) {
                                throw new IllegalStateException(String.format("Property Detector custom Bean not found with name '%s'", customDetectorBeanName));
                            }
                            log.info("Property Detector custom Bean not found with name '{}'. Initializing Default Property Detector", customDetectorBeanName);
                            return createDefault(environment);
                        }));
    }

    /**
     * <p>Constructor for DefaultLazyPropertyDetector.</p>
     *
     * @param environment a {@link ConfigurableEnvironment} object
     */
    public DefaultLazyPropertyDetector(ConfigurableEnvironment environment) {
        singleton = new Singleton<>(() -> createDefault(environment));
    }

    private DefaultPropertyDetector createDefault(ConfigurableEnvironment environment) {
        JasyptEncryptorConfigurationProperties props = JasyptEncryptorConfigurationProperties.bindConfigProps(environment);
        return new DefaultPropertyDetector(props.getProperty().getPrefix(), props.getProperty().getSuffix());
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEncrypted(String property) {
        return singleton.get().isEncrypted(property);
    }

    /** {@inheritDoc} */
    @Override
    public String unwrapEncryptedValue(String property) {
        return singleton.get().unwrapEncryptedValue(property);
    }
}
