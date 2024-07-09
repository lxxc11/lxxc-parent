package com.lvxc.enc.encryptor;

/**
 * {@link org.jasypt.encryption.StringEncryptor} version of {@link SimpleAsymmetricByteEncryptor} that just relies on
 * delegation from {@link ByteEncryptorStringEncryptorDelegate} and provides a constructor for {@link SimpleAsymmetricConfig}
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class SimpleAsymmetricStringEncryptor extends ByteEncryptorStringEncryptorDelegate {

    /**
     * <p>Constructor for SimpleAsymmetricStringEncryptor.</p>
     *
     * @param delegate a {@link SimpleAsymmetricByteEncryptor} object
     */
    public SimpleAsymmetricStringEncryptor(SimpleAsymmetricByteEncryptor delegate) {
        super(delegate);
    }

    /**
     * <p>Constructor for SimpleAsymmetricStringEncryptor.</p>
     *
     * @param config a {@link SimpleAsymmetricConfig} object
     */
    public SimpleAsymmetricStringEncryptor(SimpleAsymmetricConfig config) {
        super(new SimpleAsymmetricByteEncryptor(config));
    }
}
