package com.lvxc.enc.detector;

import com.lvxc.enc.EncryptablePropertyDetector;
import org.springframework.util.Assert;

/**
 * Default property detector that detects encrypted property values with the format "$prefix$encrypted_value$suffix"
 * Default values are "ENC(" and ")" respectively.
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class DefaultPropertyDetector implements EncryptablePropertyDetector {

    private String prefix = "HSENC(";
    private String suffix = ")";

    /**
     * <p>Constructor for DefaultPropertyDetector.</p>
     */
    public DefaultPropertyDetector() {
    }

    /**
     * <p>Constructor for DefaultPropertyDetector.</p>
     *
     * @param prefix a {@link String} object
     * @param suffix a {@link String} object
     */
    public DefaultPropertyDetector(String prefix, String suffix) {
        Assert.notNull(prefix, "Prefix can't be null");
        Assert.notNull(suffix, "Suffix can't be null");
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEncrypted(String property) {
        if (property == null) {
            return false;
        }
        final String trimmedValue = property.trim();
        return (trimmedValue.startsWith(prefix) &&
                trimmedValue.endsWith(suffix));
    }

    /** {@inheritDoc} */
    @Override
    public String unwrapEncryptedValue(String property) {
        return property.substring(
                prefix.length(),
                (property.length() - suffix.length()));
    }
}
