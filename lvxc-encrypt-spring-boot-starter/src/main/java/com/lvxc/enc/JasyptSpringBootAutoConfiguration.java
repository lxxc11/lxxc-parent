package com.lvxc.enc;

import com.lvxc.enc.configuration.EnableEncryptablePropertiesConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * <p>JasyptSpringBootAutoConfiguration class.</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
@Configuration
@Import(EnableEncryptablePropertiesConfiguration.class)
public class JasyptSpringBootAutoConfiguration {
}
