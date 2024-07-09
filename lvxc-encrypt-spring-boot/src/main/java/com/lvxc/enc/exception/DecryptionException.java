package com.lvxc.enc.exception;

/**
 * <p>DecryptionException class.</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class DecryptionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for DecryptionException.</p>
     *
     * @param message a {@link String} object
     */
    public DecryptionException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for DecryptionException.</p>
     *
     * @param message a {@link String} object
     * @param cause a {@link Throwable} object
     */
    public DecryptionException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
