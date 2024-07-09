package com.lvxc.es.exception;

/**
 * <p>DecryptionException class.</p>
 *
 * @author Ulises Bocchio
 * @version $Id: $Id
 */
public class EsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * <p>Constructor for EsException.</p>
     *
     * @param message a {@link String} object
     */
    public EsException(final String message) {
        super(message);
    }

    /**
     * <p>Constructor for EsException.</p>
     *
     * @param message a {@link String} object
     * @param cause a {@link Throwable} object
     */
    public EsException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
