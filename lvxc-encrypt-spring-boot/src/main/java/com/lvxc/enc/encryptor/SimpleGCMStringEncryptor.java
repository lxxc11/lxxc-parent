package com.lvxc.enc.encryptor;

/**
 * {@link org.jasypt.encryption.StringEncryptor} version of {@link SimpleAsymmetricByteEncryptor} that just relies on
 * delegation from {@link ByteEncryptorStringEncryptorDelegate} and provides a constructor for {@link SimpleAsymmetricConfig}
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class SimpleGCMStringEncryptor extends ByteEncryptorStringEncryptorDelegate {

    /**
     * <p>Constructor for SimpleGCMStringEncryptor.</p>
     *
     * @param delegate a {@link SimpleAsymmetricByteEncryptor} object
     */
    public SimpleGCMStringEncryptor(SimpleAsymmetricByteEncryptor delegate) {
        super(delegate);
    }

    /**
     * <p>Constructor for SimpleGCMStringEncryptor.</p>
     *
     * @param config a {@link SimpleGCMConfig} object
     */
    public SimpleGCMStringEncryptor(SimpleGCMConfig config) {
        super(new SimpleGCMByteEncryptor(config));
    }
}
