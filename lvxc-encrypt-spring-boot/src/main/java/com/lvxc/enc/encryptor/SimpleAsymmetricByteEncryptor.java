package com.lvxc.enc.encryptor;

import com.lvxc.enc.util.AsymmetricCryptography;
import com.lvxc.enc.util.Singleton;
import org.jasypt.encryption.ByteEncryptor;

import java.security.PrivateKey;
import java.security.PublicKey;

/**
 * Vanilla implementation of an asymmetric encryptor that relies on {@link AsymmetricCryptography}
 * Keys are lazily loaded from {@link SimpleAsymmetricConfig}
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class SimpleAsymmetricByteEncryptor implements ByteEncryptor {

    private final AsymmetricCryptography crypto;
    private final Singleton<PublicKey> publicKey;
    private final Singleton<PrivateKey> privateKey;

    /**
     * <p>Constructor for SimpleAsymmetricByteEncryptor.</p>
     *
     * @param config a {@link SimpleAsymmetricConfig} object
     */
    public SimpleAsymmetricByteEncryptor(SimpleAsymmetricConfig config) {
        crypto = new AsymmetricCryptography(config.getResourceLoader());
        privateKey = Singleton.fromLazy(crypto::getPrivateKey, config::loadPrivateKeyResource, config::getPrivateKeyFormat);
        publicKey = Singleton.fromLazy(crypto::getPublicKey, config::loadPublicKeyResource, config::getPublicKeyFormat);
    }

    /** {@inheritDoc} */
    @Override
    public byte[] encrypt(byte[] message) {
        return this.crypto.encrypt(message, publicKey.get());
    }

    /** {@inheritDoc} */
    @Override
    public byte[] decrypt(byte[] encryptedMessage) {
        return this.crypto.decrypt(encryptedMessage, privateKey.get());
    }
}
